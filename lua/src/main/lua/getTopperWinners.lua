--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 12/22/16
-- Time: 4:23 PM
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

ngx.log(ngx.INFO, "start to get top winner")

red:init_pipeline()
for key,itemId in ipairs(itemIds) do
    red:zrange(itemId..":DEFAULT:TOP",0,-1)
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
        end
        for j=1, #res[i] do
            local viObj = cjson.decode(res[i][j])
            local viVal = {}
            viVal["vendorItemId"]= viObj["vendorItemId"]
            viVal["itemId"]= itemIds[i]
            viVal["vendorId"] = viObj["vendorId"]
            table.insert(winnerResponse[itemIds[i]]["winners"], viVal)
        end
    end
end
ngx.say(cjson.encode(winnerResponse))

local ok, err = red:set_keepalive(ngx.var.redis_keepalive, ngx.var.redis_poolSize)
if not ok then
    ngx.log(ngx.ERR,"set keepalive failed:", err)
    return
end
