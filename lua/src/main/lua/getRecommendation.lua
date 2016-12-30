--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 12/28/16
-- Time: 6:01 PM
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

local itemIds = split(ngx.var.itemIds,",")
local recommendRes = {}

if #itemIds < 1 then
    --empty response
    ngx.say("{}")
end

local channel = ngx.var.channel
if channel then
    channel = "DEFAULT"
end

red:init_pipeline()
for key,itemId in ipairs(itemIds) do
    red:get(itemId..":"..channel..":REC")
end
local res, err= red:commit_pipeline()

if err then
    --if can not connect to reids, then go to tomcat
    --local res = ngx.location.capture(ngx.var.redis_fallbackURL)
    --ngx.say(handleTomcatResponse(res,nullResponse))
else
    for i=1, #res do
        recommendRes[itemIds[i]] = {}
        local viObj = cjson.decode(res[i])
        local viVal = {}
        viVal["vendorItemId"]= viObj["vendorItemId"]
        viVal["itemId"]= itemIds[i]
        viVal["vendorId"] = viObj["vendorId"]
        viVal["reason"] = viObj["reason"]
        table.insert(recommendRes[itemIds[i]], viVal)
    end
end

ngx.say(cjson.encode(recommendRes))

local ok, err = red:set_keepalive(ngx.var.redis_keepalive, ngx.var.redis_poolSize)
if not ok then
    ngx.log(ngx.ERR,"set keepalive failed:", err)
    return
end
