--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 12/24/16
-- Time: 10:12 PM
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
    red:zrange(itemId..":DEFAULT",0,-1)
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
            for j=1, #res[i] do
                totalWinners=totalWinners+1
                --only fetch the value within page range
                if page*size < j and (page+1)*size >= j then
                    local viObj = cjson.decode(res[i][j])
                    local viVal = {}
                    viVal["vendorItemId"]= viObj["vendorItemId"]
                    viVal["itemId"]= itemIds[i]
                    viVal["vendorId"] = viObj["vendorId"]
                    table.insert(winnerResponse[itemIds[i]]["winners"], viVal)
                end
            end

            local totalPages=math.ceil(totalWinners/size)

            if page*size >= totalWinners then
                --out of range
                local invalidResponse = {}
                invalidResponse["code"]="INVALID_ARGUMENT"
                invalidResponse["message"]="fromIndex("..page*size..") >= toIndex("..totalWinners..")"
                ngx.say(cjson.encode(invalidResponse))
                return
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
