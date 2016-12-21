package com.steve.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by stevexu on 12/21/16.
 */
public class JedisTest {

    public static void main(String[] args) {
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
        Map<String, Double> viMap = new HashMap<String, Double>();

        String key = "200";
        jedis.del(key);
        viMap.put("5500000397", 1d);
        viMap.put("5500000398", 1d);
        viMap.put("5500000400", 2d);
        viMap.put("5500000402", 3d);
        viMap.put("5500000407", 3d);
        viMap.put("5500000405", 3d);
        viMap.put("5500000399", 4d);
        viMap.put("5500000403", 4d);
        viMap.put("5500000406", 4d);
        viMap.put("5500000410", 5d);
        viMap.put("5500000412", 6d);
        viMap.put("5500000413", 7d);
        viMap.put("5500000415", 7d);
        viMap.put("5500000396", 8d);
        jedis.zadd(key, viMap);

        String key1 = "200,APP";
        jedis.del(key1);
        viMap.clear();
        viMap.put("5500000397", 1d);
        viMap.put("5500000398", 1d);
        viMap.put("5500000400", 2d);
        viMap.put("5500000402", 3d);
        viMap.put("5500000407", 3d);
        viMap.put("5500000405", 3d);
        viMap.put("5500000399", 4d);
        viMap.put("5500000403", 4d);
        viMap.put("5500000406", 4d);
        viMap.put("5500000410", 5d);
        jedis.zadd(key1, viMap);

        String key2 = "200,MOBILE_WEB";
        jedis.del(key2);
        viMap.clear();
        viMap.put("5500000397", 1d);
        viMap.put("5500000398", 1d);
        viMap.put("5500000402", 2d);
        viMap.put("5500000407", 3d);
        viMap.put("5500000405", 3d);
        viMap.put("5500000399", 4d);
        viMap.put("5500000403", 4d);
        viMap.put("5500000410", 5d);
        jedis.zadd(key2, viMap);

        String key3 = "200,WEB";
        jedis.del(key3);
        viMap.clear();
        viMap.put("5500000397", 1d);
        viMap.put("5500000402", 2d);
        viMap.put("5500000405", 3d);
        viMap.put("5500000399", 4d);
        viMap.put("5500000410", 5d);
        viMap.put("5500000412", 6d);
        jedis.zadd(key3, viMap);

        String key4 = "201";
        jedis.del(key4);
        viMap.clear();
        viMap.put("5500000505", 1d);
        viMap.put("5500000501", 2d);
        viMap.put("5500000502", 3d);
        viMap.put("5500000507", 3d);
        viMap.put("5500000505", 3d);
        viMap.put("5500000599", 4d);
        viMap.put("5500000503", 4d);
        viMap.put("5500000508", 4d);
        viMap.put("5500000510", 5d);
        viMap.put("5500000512", 6d);
        viMap.put("5500000516", 7d);
        viMap.put("5500000515", 7d);
        jedis.zadd(key4, viMap);

        String key5 = "201,APP";
        jedis.del(key5);
        viMap.clear();
        viMap.put("5500000505", 1d);
        viMap.put("5500000501", 2d);
        viMap.put("5500000502", 3d);
        viMap.put("5500000507", 3d);
        viMap.put("5500000505", 3d);
        viMap.put("5500000503", 4d);
        viMap.put("5500000508", 4d);
        viMap.put("5500000510", 5d);
        viMap.put("5500000512", 6d);
        viMap.put("5500000515", 7d);
        jedis.zadd(key5, viMap);

        String key6 = "201,WEB";
        jedis.del(key6);
        viMap.clear();
        viMap.put("5500000505", 1d);
        viMap.put("5500000501", 2d);
        viMap.put("5500000502", 3d);
        viMap.put("5500000507", 3d);
        viMap.put("5500000505", 3d);
        viMap.put("5500000599", 4d);
        viMap.put("5500000508", 4d);
        viMap.put("5500000510", 5d);
        viMap.put("5500000512", 6d);
        jedis.zadd(key6, viMap);

        String key7 = "201,MOBILE_WEB";
        jedis.del(key7);
        viMap.clear();
        viMap.put("5500000505", 1d);
        viMap.put("5500000501", 2d);
        viMap.put("5500000505", 3d);
        viMap.put("5500000599", 4d);
        viMap.put("5500000510", 5d);
        viMap.put("5500000512", 6d);
        viMap.put("5500000516", 7d);
        jedis.zadd(key7, viMap);

        String key8 = "202";
        jedis.del(key8);
        viMap.clear();
        viMap.put("5500000605", 1d);
        viMap.put("5500000601", 2d);
        viMap.put("5500000605", 2d);
        viMap.put("5500000699", 3d);
        viMap.put("5500000610", 3d);
        viMap.put("5500000612", 4d);
        viMap.put("5500000616", 5d);
        jedis.zadd(key8, viMap);

        String key9 = "202,APP";
        jedis.del(key9);
        viMap.clear();
        viMap.put("5500000605", 1d);
        viMap.put("5500000601", 2d);
        viMap.put("5500000699", 3d);
        viMap.put("5500000612", 4d);
        viMap.put("5500000616", 5d);
        jedis.zadd(key9, viMap);

        String key10 = "202,MOBILE_WEB";
        jedis.del(key10);
        viMap.clear();
        jedis.zadd(key10, viMap);

        pool.returnResource(jedis);
    }

}
