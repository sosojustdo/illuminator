package com.steve.redis;

import org.apache.commons.lang.time.StopWatch;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.List;
import java.util.ResourceBundle;

/**
 * @author stevexu
 * @Since 9/8/17
 */


public class PipelineTest {

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

        StopWatch watch=new  StopWatch();
        watch.start();
        Pipeline pipeline = jedis.pipelined();
        pipeline.multi();

        String key1 = "W:1106";
        String key2 = "W:1106:U";

        for(int i = 0; i<=10000; i++){
            pipeline.zrange(key1, 0, -1);
            pipeline.zrange(key2, 0, -1);
        }
        List<Object> results = pipeline.syncAndReturnAll();

        pool.returnResource(jedis);
        watch.stop();
    }

}
