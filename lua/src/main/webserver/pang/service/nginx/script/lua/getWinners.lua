--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 12/25/16
-- Time: 5:45 PM
-- To change this template use File | Settings | File Templates.
--

local function getRankAndViMap(res, i, itemIds, cjson)
    local lastScore = 0
    local viTempValue = {}
    local rank = 0
    local tmp = {}
    for j=1, #res[i] do
        if j%2 == 0 then
            --score
            if res[i][j] ~= lastScore then
                rank = rank+1
            end

            if tmp[rank] == nil then
                tmp[rank] = {}
                table.insert(tmp[rank],viTempValue)
            else
                table.insert(tmp[rank],viTempValue)
            end
            lastScore = res[i][j]
        else
            --value
            local viObj = cjson.decode(res[i][j])
            viTempValue = {}
            viTempValue["vendorItemId"]= viObj["vi"]
            viTempValue["itemId"]= tonumber(itemIds[i])
            viTempValue["vendorId"] = viObj["v"]
        end
    end
    return tmp
end

function releaseConnection(red)
    local ok,err = red:set_keepalive(ngx.var.redis_keepalive, ngx.var.redis_poolSize)
    if not ok then
        ngx.log(ngx.ERR,"set keepalive failed:", err)
    end
end

ngx.header.content_type = 'application/json;charset=UTF-8'
local cjson = require "cjson"
local redis = require "redis"
local util = require "util"
local upstream = require "ngx.upstream"
local http = require "http"
local args = ngx.req.get_uri_args()

local itemIdStr = util.getConcatStrFromUrlArgs(args, "itemIds")

if itemIdStr == nil then
    --empty response
    ngx.say("{}")
    return
end

local itemIds = util.split(ngx.unescape_uri(itemIdStr),",")

local winnerResponse = {}

local missingItemIdList={}

local page=0;
local size=5;
if #ngx.var.page >0 then
    page = ngx.var.page
end
if #ngx.var.size >0 then
    size = ngx.var.size
end

if tonumber(page) == nil or tonumber(size) == nil or tonumber(page)<0 or tonumber(size)<1 then
    --invalid page and size arguments
    return
end

local red = redis:new()
local host,port,err = util.getRandomRedisNode()
if err then
    ngx.log(ngx.ERR,"Failed to get redis instance information, trying to fallback and get response from appserver", err)
    util.fallbackToTomcat(upstream,http)
    return
end

red:set_timeout(ngx.var.redis_timeout)
ngx.log(ngx.ERR,"trying to get data from:"..host..":"..port)

local ok, err = red:connect(host,port)
if not ok then
    ngx.log(ngx.ERR,"redis connection error, trying to fallback and get response from appserver", err)
    util.fallbackToTomcat(upstream,http)
    return
end

local channel = ngx.var.channel
if channel == "" then
    channel = "DEF"
end

red:init_pipeline()
for key,itemId in ipairs(itemIds) do
    red:zrange("W:"..itemId..":"..util.getKeyStrFromChannel(channel),0,-1,"WITHSCORES")
end
local res, err= red:commit_pipeline()

if not res then
    ngx.log(ngx.ERR,"fetch data from redis error, trying to fallback and get response from appserver", err)
    util.fallbackToTomcat(upstream,http)
    return
else
    for i=1, #res do
        winnerResponse[itemIds[i]] = {}
        if #res[i] > 0 then
            local tmp = getRankAndViMap(res,i,itemIds,cjson)

            --shuffle
            local shuffledWinners = util.shuffle(tmp)

            --deduplicate
            local dedupWinners = util.deduplicate(shuffledWinners)
            local totalWinners = #dedupWinners

            local totalPages = math.ceil(totalWinners/size)

            if page*size > totalWinners then
                --out of range
                local invalidResponse = {}
                invalidResponse["code"]="INVALID_ARGUMENT"
                invalidResponse["message"]="fromIndex("..page*size..") > toIndex("..totalWinners..")"
                ngx.status = ngx.HTTP_BAD_REQUEST
                ngx.say(cjson.encode(invalidResponse))
                ngx.exit(ngx.HTTP_BAD_REQUEST)
                return
            end

            for d=1, #dedupWinners do
                if page*size < d and (page+1)*size >= d then
                    if winnerResponse[itemIds[i]]["winners"] == nil then
                        winnerResponse[itemIds[i]]["winners"] = {}
                    end
                    table.insert(winnerResponse[itemIds[i]]["winners"], dedupWinners[d])
                end
            end

            winnerResponse[itemIds[i]]["totalWinners"]=totalWinners
            winnerResponse[itemIds[i]]["totalPages"]=totalPages
        else
            winnerResponse[itemIds[i]]["totalWinners"]=0
            winnerResponse[itemIds[i]]["totalPages"]=0
            table.insert(missingItemIdList, itemIds[i])
        end
    end
    ngx.say(cjson.encode(winnerResponse))
end

releaseConnection(red)

if(#missingItemIdList > 0) then
    local red = redis:new()
    local ok, err = red:connect(ngx.var.redis_instrument_host,ngx.var.redis_instrument_port)
    if not ok then
        return
    end
    for i=1, #missingItemIdList do
        local res, err = red:sismember(ngx.var.missing_winner_key, missingItemIdList[i])
        if err then
            util.repair(missingItemIdList, upstream ,http)
            return
        else
            if res == 1 then
                --doesn't need to go to tomcat
                table.remove(missingItemIdList, i)
            end
        end
    end
    util.repair(missingItemIdList, upstream ,http)
    releaseConnection(red)
end


