package com.steve.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by stevexu on 4/20/17.
 */
public class RedisKeyLimitTest {

    public static void main(String[] args){
        ResourceBundle bundle = ResourceBundle.getBundle("redis");
        if (bundle == null) {
            throw new IllegalArgumentException("[redis.properties] is not found!");
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxTotal")));
        config.setMinIdle(Integer.valueOf(bundle.getString("redis.pool.minIdle")));
        config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
        config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWait")));
        config.setTestWhileIdle(Boolean.valueOf(bundle.getString("redis.pool.testWhileIdle")));
        config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));
        JedisPool pool = new JedisPool(config, bundle.getString("redis.ip"), Integer.valueOf(bundle.getString("redis.port")));

        Jedis jedis = pool.getResource();

        Pipeline pipeline = jedis.pipelined();
        pipeline.multi();

        String key = "emptyset";
        for(long i = 3000000000L; i<=3001000000L; i++){
            pipeline.sadd(key, String.valueOf(i));
        }
        pipeline.exec();

        pipeline.sync();

        pool.returnResource(jedis);
    }

}
