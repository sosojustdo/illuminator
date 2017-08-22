--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 12/28/16
-- Time: 6:01 PM
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
local recommendRes = {}

local red = redis:new()

local host,port,err = util.getRandomRedisNode()
if err then
    ngx.log(ngx.ERR,"Failed to get redis instance information, trying to fallback and get response from appserver", err)
    util.fallbackToTomcat(upstream,http)
    return
end

local ok, err = red:connect(host,port)
if not ok then
    ngx.log(ngx.ERR,"redis connection error, trying to fallback and get response from appserver", err)
    util.fallbackToTomcat(upstream, http)
    return
end

local channel = ngx.var.channel
if channel == "" then
    channel = "DEF"
end

red:init_pipeline()
for key,itemId in ipairs(itemIds) do
    red:get("R:"..itemId..":"..util.getKeyStrFromChannel(channel))
end
local res, err= red:commit_pipeline()

if not res then
    ngx.log(ngx.ERR,"fetch data from redis error, trying to fallback and get response from appserver", err)
    util.fallbackToTomcat(upstream, http)
    return
else
    for i=1, #res do
        recommendRes[itemIds[i]] = {}
        if res[i] ~= ngx.null then
           local viObj = cjson.decode(res[i])
           local viVal = {}
           viVal["vendorItemId"]= viObj["vi"]
           viVal["itemId"]= tonumber(itemIds[i])
           viVal["vendorId"] = viObj["v"]
           viVal["reason"] = viObj["r"]
           recommendRes[itemIds[i]] = viVal
        end
    end
    ngx.say(cjson.encode(recommendRes))
end

releaseConnection(red)