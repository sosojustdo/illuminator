--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 5/17/17
-- Time: 1:09 PM
-- To change this template use File | Settings | File Templates.
--
local redis_lib = require "redis"
local sentinel_lib = require "sentinel"

local sentinel_address = {
    { host = "127.0.0.1", port = 26379 },
    { host = "127.0.0.1", port = 26380 },
    { host = "127.0.0.1", port = 26381 },
}
local redis_sentinel_poolname = "redis-buybox-winner-cache"
local redis_time_out= "1000"

local discover_allnodes
local try_hosts
local resty_lock = require "resty.lock"
local redis_shared_nodes = ngx.shared.redis_nodes
local interval = 3

discover_allnodes = function(premature, redis, sentinel)
    if not premature then
        local ok, err = ngx.timer.at(interval, discover_allnodes, redis_lib, sentinel_lib)
        if not ok then
            ngx.log(ngx.ERR, "failed to create discover nodes timer:", err)
            return
        end

        local updated = redis_shared_nodes:get("updated")
        if updated then
            return
        end

        local lock, err = resty_lock:new("redis_config_locks")
        if not lock then
            ngx.log(ngx.ERR, "failed to create lock: ", err)
            return
        end

        local elapsed, err = lock:lock("redis_config")
        if not elapsed then
            ngx.log(ngx.ERR,"failed to acquire the lock: ", err)
            return
        end

        local updated = redis_shared_nodes:get("updated")
        if updated then
            local ok, err = lock:unlock()
            if not ok then
                ngx.log(ngx.ERR, "failed to unlock: ", err)
            end
            return
        end

        local red, err = try_hosts(redis)
        if err then
            ngx.log(ngx.ERR, "failed to connect sentinel: ", table.concat(err,";"))
            local ok, err = lock:unlock()
            if not ok then
                ngx.log(ngx.ERR, "failed to unlock: ", err)
            end
            return
        end

        local redis_nodes = {}


        -- We will try only read from slave, if no good node , trying to add master
        local slaves, err = sentinel.get_slaves(red, redis_sentinel_poolname)
        if not slaves then
            ngx.log(ngx.ERR, "fetch slave data from sentinel error:", err)
        else
            for _,slave in ipairs(slaves) do
                table.insert(redis_nodes,slave.host..":"..slave.port)
            end
        end

        if #redis_nodes<1 then
            ngx.log(ngx.ERR, "There is no good redis slave nodes, will try to fetch master")
            local master, err = sentinel.get_master(red, redis_sentinel_poolname)
            if not master then
                ngx.log(ngx.ERR, "fetch master data from sentinel error:", err)
            else
                table.insert(redis_nodes,master.host..":"..master.port)
            end
        end

        local ok, err = red:close()
        if not ok then
            ngx.log(ngx.ERR, "failed to close sentinel connection:", err)
        end

        redis_shared_nodes:set("nodes", table.concat(redis_nodes, ","))
        redis_shared_nodes:set("updated", 1, interval-1)

        local ok, err = lock:unlock()
        if not ok then
            ngx.log(ngx.ERR, "failed to unlock:", err)
        end

        ngx.log(ngx.NOTICE , "Currently the nodes are:"..redis_shared_nodes:get("nodes"))
    end
end

try_hosts = function(redis)
    local errors = {}
    for i, address in ipairs(sentinel_address) do
        local red = redis:new()
        red:set_timeout(redis_time_out)
        local ok, err = red:connect(address["host"], address["port"])
        if ok then
            return red, nil
        else
            table.insert(errors, err)
        end
    end
    return nil, errors
end

local ok, err = ngx.timer.at(0, discover_allnodes, redis_lib, sentinel_lib)
if not ok then
    ngx.log(ngx.ERR, "failed to create discover nodes timer:", err)
end

