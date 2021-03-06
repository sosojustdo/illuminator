--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 12/24/16
-- Time: 10:12 PM
-- To change this template use File | Settings | File Templates.
--

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

local itemIds = util.split(ngx.unescape_uri(util.getConcatStrFromUrlArgs(args, "itemIds")),",")
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

local channel = ngx.var.channel
if channel == "" then
    channel = "DEF"
end

local red = redis:new()
local host,port,err = util.getRandomRedisNode()
if err then
    ngx.log(ngx.ERR,"Failed to get redis instance information, trying to fallback and get response from appserver", err)
    util.fallbackToTomcat(upstream,http)
    return
end

red:set_timeout(ngx.var.redis_timeout)
local ok, err = red:connect(host,port)
if not ok then
    ngx.log(ngx.ERR,"redis connection error, trying to fallback and get response from appserver", err)
    util.fallbackToTomcat(upstream, http)
    return
end

red:init_pipeline()
for key,itemId in ipairs(itemIds) do
    red:zrange("W:"..itemId..":"..util.getKeyStrFromChannel(channel),0,-1)
end
local res, err= red:commit_pipeline()

if not res then
    ngx.log(ngx.ERR,"fetch data from redis error, trying to fallback and get response from appserver", err)
    util.fallbackToTomcat(upstream, http)
    return
else
    for i=1, #res do
        winnerResponse[itemIds[i]] = {}
        if #res[i] > 0 then
            local totalWinners=0
            for j=1, #res[i] do
                totalWinners=totalWinners+1
                --only fetch the value within page range
                if page*size < j and (page+1)*size >= j then
                    local viObj = cjson.decode(res[i][j])
                    local viVal = {}
                    viVal["vendorItemId"]= viObj["vi"]
                    viVal["itemId"]= tonumber(itemIds[i])
                    viVal["vendorId"] = viObj["v"]
                    if winnerResponse[itemIds[i]]["winners"] == nil then
                        winnerResponse[itemIds[i]]["winners"] = {}
                    end
                    table.insert(winnerResponse[itemIds[i]]["winners"], viVal)
                end
            end

            local totalPages=math.ceil(totalWinners/size)

            if page*size > totalWinners then
                --out of range
                local invalidResponse = {}
                invalidResponse["code"]="INVALID_ARGUMENT"
                invalidResponse["message"]="fromIndex("..page*size..") > toIndex("..totalWinners..")"
                ngx.status = ngx.HTTP_BAD_REQUEST
                ngx.say(cjson.encode(invalidResponse))
                ngx.exit(ngx.HTTP_BAD_REQUEST)
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


