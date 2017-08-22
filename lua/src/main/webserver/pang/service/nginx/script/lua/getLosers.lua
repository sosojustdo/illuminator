--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 12/27/16
-- Time: 10:34 PM
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
local itemIds = util.split(ngx.unescape_uri(util.getConcatStrFromUrlArgs(args, "itemIds")),",")
if #itemIds < 1 then
    --empty response
    ngx.say("{}")
    return
end

local topred = redis:new()

topred:set_timeout(ngx.var.redis_timeout)
local ok, err = topred:connect(ngx.var.redis_top_host,ngx.var.redis_top_port)
if not ok then
    ngx.log(ngx.ERR,"redis connection error, trying to fallback and get response from appserver", err)
    util.fallbackToTomcat(upstream, http)
    return
end

local topWinners = {}
local loserResponse = {}
local channels = {"APP","WEB","MOBILE_WEB","APP_PUSH" }

local function getTopWinnersByChannel(itemIds, topres, channels)
    local topWinners = {}
    for i=1, #itemIds do
        topWinners[itemIds[i]] = {}
        for c=1, #channels do
            topWinners[itemIds[i]][channels[c]]={}
            for v=1, #topres[(i-1)*#channels+c] do
                local viObj = cjson.decode(topres[(i-1)*#channels+c][v])
                local topVi =  viObj["vi"]
                topWinners[itemIds[i]][channels[c]][topVi] = true
            end
        end
    end
    return topWinners
end

local function getRankAndViMap(res, i, itemIds, cjson, channels, c)
    local lastScore = 0
    local rank = 0
    local tmp = {}
    local viTempValue = {}
    for v=1, #res[(i-1)*#channels+c] do
        if v%2 == 0 then
            --score
            if res[(i-1)*#channels+c][v] ~= lastScore then
                rank = rank+1
            end

            if tmp[rank] == nil then
                tmp[rank] = {}
                table.insert(tmp[rank],viTempValue)
            else
                table.insert(tmp[rank],viTempValue)
            end
            lastScore = res[(i-1)*#channels+c][v]
        else
            --value
            local viObj = cjson.decode(res[(i-1)*#channels+c][v])
            viTempValue = {}
            viTempValue["vendorItemId"]= viObj["vi"]
            viTempValue["itemId"]= tonumber(itemIds[i])
            viTempValue["vendorId"] = viObj["v"]
        end
    end
    return tmp
end

topred:init_pipeline()
for key,itemId in ipairs(itemIds) do
    for key,channel in ipairs(channels) do
        topred:zrange("T:"..itemId..":"..util.getKeyStrFromChannel(channel),0,-1)
    end
end
local topres, toperr= topred:commit_pipeline()

if not topres then
    ngx.log(ngx.ERR,"fetch data from redis error, trying to fallback and get response from appserver", toperr)
    util.fallbackToTomcat(upstream,http)
    return
else
    topWinners = getTopWinnersByChannel(itemIds,topres,channels)
end
releaseConnection(topred)

local loserResponse = {}

local red = redis:new()

red:set_timeout(ngx.var.redis_timeout)
local ok, err = red:connect(ngx.var.redis_winner_host,ngx.var.redis_winner_port)
if not ok then
    ngx.log(ngx.ERR,"redis connection error, trying to fallback and get response from appserver", err)
    util.fallbackToTomcat(upstream, http)
    return
end


red:init_pipeline()
for key,itemId in ipairs(itemIds) do
    for key,channel in ipairs(channels) do
        red:zrange("W:"..itemId..":"..util.getKeyStrFromChannel(channel),0,-1,"WITHSCORES")
    end
end
local res, err= red:commit_pipeline()

if not res then
    ngx.log(ngx.ERR,"fetch data from redis error, trying to fallback and get response from appserver", err)
    util.fallbackToTomcat(upstream,http)
    return
else
    for i=1, #itemIds do
        local totalLosers=0
        --array to save score and vi map
        loserResponse[itemIds[i]]={}
        loserResponse[itemIds[i]]["losers"]={}
        for c=1, #channels do
            loserResponse[itemIds[i]]["losers"][channels[c]] = {}
            local tmp = getRankAndViMap(res, i, itemIds, cjson, channels, c)

            --shuffle
            local shuffledWinners = util.shuffle(tmp)

            --deduplicate
            local dedupWinners = util.deduplicate(shuffledWinners)

            local totalLosers = 0
            for d=1, #dedupWinners do
                local vi = dedupWinners[d]["vendorItemId"]
                --loser should remove the top winners
                if not topWinners[itemIds[i]][channels[c]][vi] then
                    totalLosers = totalLosers+1
                    table.insert(loserResponse[itemIds[i]]["losers"][channels[c]],dedupWinners[d])
                end
            end
            if #loserResponse[itemIds[i]]["losers"][channels[c]]<1 then
                loserResponse[itemIds[i]]["losers"][channels[c]] = cjson.empty_array
            end
        end

        loserResponse[itemIds[i]]["itemId"] = itemIds[i]

        local time=os.time() * 1000;
        loserResponse[itemIds[i]]["eventTime"] = time
    end
    ngx.say(cjson.encode(loserResponse))
end

releaseConnection(red)
