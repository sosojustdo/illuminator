package com.steve.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by stevexu on 12/21/16.
 */
public class JedisTest {

    public static void main(String[] args) throws JsonProcessingException {
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

        Pipeline pipeline = jedis.pipelined();

        ObjectMapper objectMapper = new ObjectMapper();


        String key = "200,DEFAULT";
        jedis.del(key);
        VendorItemDTO dto = new VendorItemDTO("5500000397","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 1d);
        dto = new VendorItemDTO("5500000398","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 1d);
        dto = new VendorItemDTO("5500000400","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 2d);
        dto = new VendorItemDTO("5500000402","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000407","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000405","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000399","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000403","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000406","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000410","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 5d);
        dto = new VendorItemDTO("5500000412","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 6d);
        dto = new VendorItemDTO("5500000413","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 7d);
        dto = new VendorItemDTO("5500000415","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 7d);
        dto = new VendorItemDTO("5500000396","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 8d);
        jedis.zadd(key, viMap);

        String key1 = "200,APP";
        jedis.del(key1);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 1d);
        dto = new VendorItemDTO("5500000398","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 1d);
        dto = new VendorItemDTO("5500000400","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 2d);
        dto = new VendorItemDTO("5500000402","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000407","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000405","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000399","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000403","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000406","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000410","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 5d);
        jedis.zadd(key1, viMap);

        String key2 = "200,MOBILE_WEB";
        jedis.del(key2);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 1d);
        dto = new VendorItemDTO("5500000398","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 1d);
        dto = new VendorItemDTO("5500000402","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 2d);
        dto = new VendorItemDTO("5500000407","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 2d);
        dto = new VendorItemDTO("5500000405","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 2d);
        dto = new VendorItemDTO("5500000399","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000403","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000406","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        jedis.zadd(key2, viMap);

        String key3 = "200,WEB";
        jedis.del(key3);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 1d);
        dto = new VendorItemDTO("5500000402","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 2d);
        dto = new VendorItemDTO("5500000405","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 2d);
        dto = new VendorItemDTO("5500000399","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000410","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000412","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 5d);
        jedis.zadd(key3, viMap);

        String key4 = "201,DEFAULT";
        jedis.del(key4);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 1d);
        dto = new VendorItemDTO("5500000501","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 2d);
        dto = new VendorItemDTO("5500000502","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000507","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000505","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000599","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000503","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000508","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000510","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 5d);
        dto = new VendorItemDTO("5500000512","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 6d);
        dto = new VendorItemDTO("5500000516","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 7d);
        dto = new VendorItemDTO("5500000515","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 7d);
        jedis.zadd(key4, viMap);

        String key5 = "201,APP";
        jedis.del(key5);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 1d);
        dto = new VendorItemDTO("5500000501","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 2d);
        dto = new VendorItemDTO("5500000502","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000507","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000505","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000503","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000508","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000510","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 5d);
        dto = new VendorItemDTO("5500000512","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 6d);
        dto = new VendorItemDTO("5500000515","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 7d);
        jedis.zadd(key5, viMap);

        String key6 = "201,WEB";
        jedis.del(key6);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 1d);
        dto = new VendorItemDTO("5500000501","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 2d);
        dto = new VendorItemDTO("5500000502","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000507","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000505","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000599","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000508","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000510","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 5d);
        dto = new VendorItemDTO("5500000512","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 6d);
        jedis.zadd(key6, viMap);

        String key7 = "201,MOBILE_WEB";
        jedis.del(key7);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 1d);
        dto = new VendorItemDTO("5500000501","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 2d);
        dto = new VendorItemDTO("5500000502","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000599","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000510","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 5d);
        dto = new VendorItemDTO("5500000512","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 6d);
        dto = new VendorItemDTO("5500000516","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 7d);
        jedis.zadd(key7, viMap);

        String key8 = "202,DEFAULT";
        jedis.del(key8);
        viMap.clear();
        viMap.put("{\"vendorItemId\":5500000605,\"vendorId\":A00010029,\"isCRV\":true}", 1d);
        viMap.put("{\"vendorItemId\":5500000601,\"vendorId\":A00010028,\"isCRV\":false}", 2d);
        viMap.put("{\"vendorItemId\":5500000605,\"vendorId\":A00010028,\"isCRV\":false}", 2d);
        viMap.put("{\"vendorItemId\":5500000699,\"vendorId\":A00010028,\"isCRV\":false}", 3d);
        viMap.put("{\"vendorItemId\":5500000610,\"vendorId\":A00010030,\"isCRV\":false}", 4d);
        viMap.put("{\"vendorItemId\":5500000612,\"vendorId\":A00010030,\"isCRV\":false}", 5d);
        viMap.put("{\"vendorItemId\":5500000616,\"vendorId\":A00010029,\"isCRV\":true}", 6d);
        dto = new VendorItemDTO("5500000605","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 1d);
        dto = new VendorItemDTO("5500000601","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 2d);
        dto = new VendorItemDTO("5500000605","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 2d);
        dto = new VendorItemDTO("5500000699","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000610","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000612","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 5d);
        dto = new VendorItemDTO("5500000616","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 6d);
        jedis.zadd(key8, viMap);

        String key9 = "202,APP";
        jedis.del(key9);
        viMap.clear();
        viMap.put("{\"vendorItemId\":5500000605,\"vendorId\":A00010029,\"isCRV\":true}", 1d);
        viMap.put("{\"vendorItemId\":5500000601,\"vendorId\":A00010028,\"isCRV\":false}", 2d);
        viMap.put("{\"vendorItemId\":5500000699,\"vendorId\":A00010028,\"isCRV\":false}", 3d);
        viMap.put("{\"vendorItemId\":5500000612,\"vendorId\":A00010030,\"isCRV\":false}", 4d);
        viMap.put("{\"vendorItemId\":5500000616,\"vendorId\":A00010029,\"isCRV\":true}", 5d);
        dto = new VendorItemDTO("5500000605","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 1d);
        dto = new VendorItemDTO("5500000601","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 2d);
        dto = new VendorItemDTO("5500000699","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 3d);
        dto = new VendorItemDTO("5500000612","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 4d);
        dto = new VendorItemDTO("5500000616","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 5d);
        jedis.zadd(key9, viMap);

        List<Object> results = pipeline.syncAndReturnAll();

        pool.returnResource(jedis);
    }

}
