--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 12/23/16
-- Time: 9:49 PM
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

local channels = util.split(ngx.unescape_uri(util.getConcatStrFromUrlArgs(args, "channels")),",")
if #channels < 1 then
    --empty response
    ngx.say("{}")
    return
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
    for key,channel in ipairs(channels) do
        red:zrange("T:"..itemId..":"..util.getKeyStrFromChannel(channel),0,-1)
    end
end
local res, err= red:commit_pipeline()

if not res then
    ngx.log(ngx.ERR,"fetch data from redis error, trying to fallback and get response from appserver", err)
    util.fallbackToTomcat(upstream, http)
    return
else
    for i=1, #itemIds do
        winnerResponse[itemIds[i]] = {}
        for c=1, #channels do
            winnerResponse[itemIds[i]][channels[c]]={}
            if #res[(i-1)*#channels+c]>0 then
                winnerResponse[itemIds[i]][channels[c]]["winners"]={}
            end
            for v=1, #res[(i-1)*#channels+c] do
               local viObj = cjson.decode(res[(i-1)*#channels+c][v])
               local viVal = {}
               viVal["vendorItemId"]= viObj["vi"]
               viVal["itemId"]= tonumber(itemIds[i])
               viVal["vendorId"] = viObj["v"]
               table.insert(winnerResponse[itemIds[i]][channels[c]]["winners"], viVal)
            end
            if(#res[(i-1)*#channels+c]) <1 then
                table.insert(missingItemIdList, itemIds[i])
            end
        end
    end
    ngx.say(cjson.encode(winnerResponse))
end

releaseConnection(red)
--[[
if(#missingItemIdList>0)then
    util.repair(missingItemIdList, upstream ,http)
end]]
