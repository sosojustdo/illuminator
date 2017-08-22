--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 12/29/16
-- Time: 8:53 PM
-- To change this template use File | Settings | File Templates.
--
local _M = {}
local shared_redis_nodes = ngx.shared.redis_nodes
_M._VERSION = '0.1'

function _M.split(str, separator)
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

math.randomseed(os.time())

local function swap(array, index1, index2)
    array[index1], array[index2] = array[index2], array[index1]
end

local function shuffleArray(array)
    local counter = #array
    while counter > 1 do
        local index = math.random(counter)
        swap(array,index,counter)
        counter = counter - 1
    end
end

function _M.shuffle(tmp)
    local shuffledWinners = {}
    for rank,val in pairs(tmp) do
        if(#val <= 1) then
            --no need to shuffle
        else
            --there is multiple vendoritem within same rank, need to shuffle
            shuffleArray(val)
        end
        for v=1, #val do
            table.insert(shuffledWinners, val[v])
        end
    end
    return shuffledWinners
end

function _M.deduplicate(shuffledWinners)
    local encounter = false
    local vendorSet = {}
    local dedupWinners  ={}
    for s=1, #shuffledWinners do
        if _M.isConsignmentRetailVendor(shuffledWinners[s]["vendorId"]) then
            --only need one vendoritem for consignment retail vendors
            if not encounter then
                table.insert(dedupWinners, shuffledWinners[s])
                encounter = true
            end
        else
            --only need one vendoritem for each vendor, for other vendoritem within same vendor, just ignore it
            local vendor = shuffledWinners[s]["vendorId"]
            if not vendorSet[vendor] then
                table.insert(dedupWinners, shuffledWinners[s])
                vendorSet[vendor] = true
            end
        end
    end
    return dedupWinners
end

_M.consignmentRetailVendorIds= {"A00010028","A00038294","A00045899","A00075170"}

function _M.isConsignmentRetailVendor(vendorId)
    for _, value in pairs(_M.consignmentRetailVendorIds) do
        if value == vendorId then
            return true
        end
    end
    return false
end

_M.channelMap = {}
_M.channelMap["DEF"] = "D"
_M.channelMap["APP"] = "A"
_M.channelMap["WEB"] = "W"
_M.channelMap["MOBILE_WEB"] = "MW"
_M.channelMap["APP_PUSH"] = "AP"

function _M.getKeyStrFromChannel(channel)
     if _M.channelMap[channel]~= nil then
         return _M.channelMap[channel]
     else
         return ""
     end
end

--[[
function _M.authentication(red)
    local redisPassword = ngx.var.redis_password
    if redisPassword == nil then
        redisPassword = ""
    end
    local reuseCount, err = red:get_reused_times()
    if 0 == reuseCount then
        local ok, err = red:auth(redisPassword)
        if not ok then
            ngx.log(ngx.ERR,"failed to auth: ", err)
            return false
        end
    elseif err then
        ngx.log(ngx.ERR,"failed to get reused times: ", err)
        return false
    end
    return true
end
]]

--[[function _M.releaseConnection(red)
    local ok, err = red:set_keepalive(ngx.var.redis_keepalive, ngx.var.redis_poolSize)
    if not ok then
        ngx.log(ngx.ERR,"set keepalive failed:", err)
        return
    end
end]]

function _M.fallbackToTomcat(upstream, http)
    if(not ngx.ctx.upstream) then
        local server, err = upstream.get_servers(ngx.var.live_upstream);
        if err or (not server) or (#server<1) then
            ngx.ctx.upstream = "127.0.0.1:10001"
        else
            ngx.ctx.upstream = server[1]["addr"]
        end
    end

    local httpc = http.new()
    local res, err = httpc:request_uri("http://"..ngx.ctx.upstream..ngx.var.request_uri,
        {
            version = 1.1,
            method = "GET",
            headers = {
                ["Content-Type"] = "application/json;charset=utf8",
                ["Accept"] = "application/json"}
        })
    if res.body == ngx.null then
        ngx.say("{}")
    else
        if res.status ~= ngx.HTTP_OK then
            ngx.status = res.status
            ngx.say(res.body)
            ngx.exit(res.status)
        else
            -- This body is from tomcat, it is already in json format.
            ngx.say(res.body)
        end
    end
end

function _M.repair(missingItemIdList, upstream, http)
    local body = "["..table.concat(missingItemIdList,",").."]"
    if(not ngx.ctx.upstream) then
        local server, err = upstream.get_servers(ngx.var.live_upstream);
        if err or (not server) or (#server<1) then
            ngx.ctx.upstream = "127.0.0.1:10001"
        else
            ngx.ctx.upstream = server[1]["addr"]
        end
    end

    function handler(premature, url, body)
        if premature then
            return
        end
        local httpc = http.new()
        local res, err = httpc:request_uri(url,
            {
                method = "POST",
                body = body,
                headers = {["Content-Type"] = "application/json;charset=utf8",["Accept"] = "application/json"}
            })
    end

    ngx.timer.at(0, handler, "http://"..ngx.ctx.upstream..ngx.var.missing_winner_repair, body)
end

function _M.getConcatStrFromUrlArgs(args, keystr)
    for key, val in pairs(args) do
        if key == keystr then
            if type(val) == "table" then
                return table.concat(val, ",")
            else
                return val
            end
        end
    end
end

function _M.getRandomRedisNode()
    local host
    local port
    local err
    local redis_nodes = shared_redis_nodes:get("nodes")
    if redis_nodes == nil then
        err = "redis nodes dictionary is empty"
        return host,port,err
    end

    local redis_nodes_array = _M.split(redis_nodes,",")
    if #redis_nodes_array < 1 then
        err =  "redis nodes dictionary is empty"
        return host,port,err
    end

    local index = math.random(#redis_nodes_array)
    local redis_node = redis_nodes_array[index]

    local lastIndex = string.find(redis_node, ":", 0)
    if not lastIndex then
        err = "invalid redis node information from shared redis dictionary"
        return host,port,err
    end
    host = string.sub(redis_node, 0, lastIndex - 1)
    port = string.sub(redis_node, lastIndex+1, string.len(redis_node))
    return host,port,err
end

return _M