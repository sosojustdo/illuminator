--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 12/22/16
-- Time: 4:23 PM
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
local winnerResponse = {}
local missingItemIdList={}

if #itemIds < 1 then
    --empty response
    ngx.say("{}")
    return
end

local channel = ngx.var.channel
if channel == "" then
    channel = "DEF"
end

local red = redis:new()

red:set_timeout(ngx.var.redis_timeout)
local ok, err = red:connect(ngx.var.redis_top_host,ngx.var.redis_top_port)
if not ok then
    ngx.log(ngx.ERR,"redis connection error, trying to fallback and get response from appserver", err)
    util.fallbackToTomcat(upstream, http)
    return
end

red:init_pipeline()
for key,itemId in ipairs(itemIds) do
    red:zrange("T:"..itemId..":"..util.getKeyStrFromChannel(channel),0,-1)
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
            winnerResponse[itemIds[i]]["winners"] = {}
        else
            table.insert(missingItemIdList, itemIds[i])
        end
        for j=1, #res[i] do
            local viObj = cjson.decode(res[i][j])
            local viVal = {}
            viVal["vendorItemId"]= viObj["vi"]
            viVal["itemId"]= tonumber(itemIds[i])
            viVal["vendorId"] = viObj["v"]
            table.insert(winnerResponse[itemIds[i]]["winners"], viVal)
        end
    end
    ngx.say(cjson.encode(winnerResponse))
end

releaseConnection(red)

--[[
if(#missingItemIdList>0)then
    util.repair(missingItemIdList, upstream ,http)
end]]
