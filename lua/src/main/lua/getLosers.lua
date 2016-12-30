--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 12/27/16
-- Time: 10:34 PM
-- To change this template use File | Settings | File Templates.
--
ngx.header.content_type = 'application/json;charset=UTF-8'
local cjson = require "cjson"
local redis = require "redis"
local util = require "util"
local red = redis:new()

red:set_timeout(ngx.var.redis_timeout)
local ok, err = red:connect(ngx.var.redis_host,ngx.var.redis_port)
if not ok then
    return
end

local itemIds = util.split(ngx.var.itemIds,",")
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
                local topVi =  viObj["vendorItemId"]
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
            viTempValue["vendorItemId"]= viObj["vendorItemId"]
            viTempValue["itemId"]= itemIds[i]
            viTempValue["vendorId"] = viObj["vendorId"]
            viTempValue["crv"] = viObj["crv"]
        end
    end
    return tmp
end

if #itemIds < 1 then
    --empty response
    ngx.say("{}")
end

local page=0;
local size=10000;
if #ngx.var.page >0 then
    page = ngx.var.page
end

if tonumber(page) == nil or tonumber(page)<0 then
    --invalid page and size arguments
    return
end

red:init_pipeline()
for key,itemId in ipairs(itemIds) do
    for key,channel in ipairs(channels) do
        red:zrange(itemId..":"..channel..":TOP",0,-1)
    end
end
local topres, toperr= red:commit_pipeline()

if toperr then
    --if can not connect to reids, then go to tomcat
    --local res = ngx.location.capture(ngx.var.redis_fallbackURL)
    --ngx.say(handleTomcatResponse(res,nullResponse))
else
    topWinners = getTopWinnersByChannel(itemIds,topres,channels)
end

local loserResponse = {}

red:init_pipeline()
for key,itemId in ipairs(itemIds) do
    for key,channel in ipairs(channels) do
        red:zrange(itemId..":"..channel,0,-1,"WITHSCORES")
    end
end
local res, err= red:commit_pipeline()

if err then
    --if can not connect to reids, then go to tomcat
    --local res = ngx.location.capture(ngx.var.redis_fallbackURL)
    --ngx.say(handleTomcatResponse(res,nullResponse))
else
    for i=1, #itemIds do
        local totalLosers=0
        --array to save score and vi map
        loserResponse[itemIds[i]]={}
        for c=1, #channels do
            loserResponse[itemIds[i]][channels[c]]={}
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
                    if page*size < totalLosers and (page+1)*size >= totalLosers then
                        table.insert(loserResponse[itemIds[i]][channels[c]],dedupWinners[d])
                    end
                end
            end

            if page*size > totalLosers and totalLosers > 0 then
                --out of range
                local invalidResponse = {}
                invalidResponse["code"]="INVALID_ARGUMENT"
                invalidResponse["message"]="fromIndex("..page*size..") > toIndex("..totalLosers..")"
                ngx.say(cjson.encode(invalidResponse))
                return
            end
        end

        local time=os.time() * 1000;
        loserResponse[itemIds[i]]["eventTime"] = time
    end
end

ngx.say(cjson.encode(loserResponse))

local ok, err = red:set_keepalive(ngx.var.redis_keepalive, ngx.var.redis_poolSize)
if not ok then
    ngx.log(ngx.ERR,"set keepalive failed:", err)
    return
end
