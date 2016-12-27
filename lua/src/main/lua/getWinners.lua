--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 12/25/16
-- Time: 5:45 PM
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
local winnerResponse = {}

if #itemIds < 1 then
    --empty response
    ngx.say("{}")
end

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

red:init_pipeline()
for key,itemId in ipairs(itemIds) do
    red:zrange(itemId..":DEFAULT",0,-1,"WITHSCORES")
end
local res, err= red:commit_pipeline()

if err then
    --if can not connect to reids, then go to tomcat
    --local res = ngx.location.capture(ngx.var.redis_fallbackURL)
    --ngx.say(handleTomcatResponse(res,nullResponse))
else
    for i=1, #res do
        winnerResponse[itemIds[i]] = {}
        if #res[i] > 0 then
            winnerResponse[itemIds[i]]["winners"] = {}
            local totalWinners=0
            --array to save score and vi map
            local tmp = {}
            local viTempValue = {}
            local lastScore = 0
            local rank = 0
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
                    viTempValue["vendorItemId"]= viObj["vendorItemId"]
                    viTempValue["itemId"]= itemIds[i]
                    viTempValue["vendorId"] = viObj["vendorId"]
                    viTempValue["crv"] = viObj["crv"]
                end
            end

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
            local totalWinners = 0
            for s=1, #shuffledWinners do
                if shuffledWinners[s]["crv"] then
                    --only need one vendoritem for consignment retail vendors
                    if not encounter then
                        --we don't need to display if it is consignment retail vendors
                        shuffledWinners[s]["crv"] = nil
                        table.insert(dedupWinners, shuffledWinners[s])
                        totalWinners = totalWinners+1
                        encounter = true
                    end
                else
                    --only need one vendoritem for each vendor, for other vendoritem within same vendor, just ignore it
                    local vendor = shuffledWinners[s]["vendorId"]
                    if not vendorSet[vendor] then
                        --we don't need to display if it is consignment retail vendors
                        shuffledWinners[s]["crv"] = nil
                        table.insert(dedupWinners, shuffledWinners[s])
                        totalWinners = totalWinners+1
                        vendorSet[vendor] = true
                    end
                end
            end

            local totalPages = math.ceil(totalWinners/size)

            if page*size >= totalWinners then
                --out of range
                local invalidResponse = {}
                invalidResponse["code"]="INVALID_ARGUMENT"
                invalidResponse["message"]="fromIndex("..page*size..") >= toIndex("..totalWinners..")"
                ngx.say(cjson.encode(invalidResponse))
                return
            end

            for d=1, #dedupWinners do
                if page*size < d and (page+1)*size >= d then
                    table.insert(winnerResponse[itemIds[i]]["winners"], dedupWinners[d])
                end
            end

            winnerResponse[itemIds[i]]["totalWinners"]=totalWinners
            winnerResponse[itemIds[i]]["totalPages"]=totalPages
        else
            winnerResponse[itemIds[i]]["totalWinners"]=0
            winnerResponse[itemIds[i]]["totalPages"]=0
        end
    end
end

ngx.say(cjson.encode(winnerResponse))

local ok, err = red:set_keepalive(ngx.var.redis_keepalive, ngx.var.redis_poolSize)
if not ok then
    ngx.log(ngx.ERR,"set keepalive failed:", err)
    return
end
