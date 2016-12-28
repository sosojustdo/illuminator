--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 12/27/16
-- Time: 10:34 PM
-- To change this template use File | Settings | File Templates.
--
function split(str, separator)
    local splitArray = {}
    if(string.len(str)<1) then
        return splitArray
    end
    local startIndex = 1
    local splitIndex = 1
    while true do
        local lastIndex = string.find(str, separator, startIndex)
        if not lastIndex then
            splitArray[splitIndex] = string.sub(str, startIndex, string.len(str))
            break
        end
        splitArray[splitIndex] = string.sub(str, startIndex, lastIndex - 1)
        startIndex = lastIndex + string.len(separator)
        splitIndex = splitIndex + 1
    end
    return splitArray
end

function swap(array, index1, index2)
    array[index1], array[index2] = array[index2], array[index1]
end

math.randomseed(os.time())
function shuffle(array)
    local counter = #array
    while counter > 1 do
        local index = math.random(counter)
        swap(array,index,counter)
        counter = counter - 1
    end
end

ngx.header.content_type = 'application/json;charset=UTF-8'
local cjson = require "cjson"
local redis = require "redis"
local red = redis:new()

red:set_timeout(ngx.var.redis_timeout)
local ok, err = red:connect(ngx.var.redis_host,ngx.var.redis_port)
if not ok then
    return
end

local itemIds = split(ngx.var.itemIds,",")
local loserResponse = {}
local topWinners = {}
local channels = {"APP","WEB","MOBILE_WEB","APP_PUSH"}

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
        local viTempValue = {}
        loserResponse[itemIds[i]]={}
        for c=1, #channels do
            loserResponse[itemIds[i]][channels[c]]={}
            local lastScore = 0
            local rank = 0
            local tmp = {}
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

            --shuffle
            local shuffledWinners = {}
            for rank,val in pairs(tmp) do
                if(#val <= 1) then
                    --no need to shuffle
                else
                    --there is multiple vendoritem within same rank, need to shuffle
                    shuffle(val)
                end
                for v=1, #val do
                    table.insert(shuffledWinners, val[v])
                end
            end

            --deduplicate
            local encounter = false
            local vendorSet = {}
            local dedupWinners  ={}
            local totalLosers = 0
            for s=1, #shuffledWinners do
                if shuffledWinners[s]["crv"] then
                    --only need one vendoritem for consignment retail vendors
                    if not encounter then
                        --we don't need to display if it is consignment retail vendors
                        shuffledWinners[s]["crv"] = nil
                        table.insert(dedupWinners, shuffledWinners[s])
                        encounter = true
                    end
                else
                    --only need one vendoritem for each vendor, for other vendoritem within same vendor, just ignore it
                    local vendor = shuffledWinners[s]["vendorId"]
                    if not vendorSet[vendor] then
                        --we don't need to display if it is consignment retail vendors
                        shuffledWinners[s]["crv"] = nil
                        table.insert(dedupWinners, shuffledWinners[s])
                        vendorSet[vendor] = true
                    end
                end
            end


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

        local time=os.time();
        loserResponse[itemIds[i]]["eventTime"] = time
    end
end

ngx.say(cjson.encode(loserResponse))

local ok, err = red:set_keepalive(ngx.var.redis_keepalive, ngx.var.redis_poolSize)
if not ok then
    ngx.log(ngx.ERR,"set keepalive failed:", err)
    return
end
