--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 12/23/16
-- Time: 9:49 PM
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
local winnerResponse = {}

if #itemIds < 1 then
    --empty response
    ngx.say("{}")
end

local channels = util.split(ngx.var.channels,",")
if #channels < 1 then
    --empty response
    ngx.say("{}")
end

red:init_pipeline()
for key,itemId in ipairs(itemIds) do
    for key,channel in ipairs(channels) do
        red:zrange(itemId..":"..channel..":TOP",0,-1)
    end
end
local res, err= red:commit_pipeline()

if err then
    --if can not connect to reids, then go to tomcat
    --local res = ngx.location.capture(ngx.var.redis_fallbackURL)
    --ngx.say(handleTomcatResponse(res,nullResponse))
else
    for i=1, #itemIds do
        winnerResponse[itemIds[i]] = {}
        for c=1, #channels do
            winnerResponse[itemIds[i]][channels[c]]={}
            winnerResponse[itemIds[i]][channels[c]]["winners"]={}
            for v=1, #res[(i-1)*#channels+c] do
               local viObj = cjson.decode(res[(i-1)*#channels+c][v])
               local viVal = {}
               viVal["vendorItemId"]= viObj["vendorItemId"]
               viVal["itemId"]= itemIds[i]
               viVal["vendorId"] = viObj["vendorId"]
               table.insert(winnerResponse[itemIds[i]][channels[c]]["winners"], viVal)
            end
        end
    end
end
ngx.say(cjson.encode(winnerResponse))

local ok, err = red:set_keepalive(ngx.var.redis_keepalive, ngx.var.redis_poolSize)
if not ok then
    ngx.log(ngx.ERR,"set keepalive failed:", err)
    return
end
