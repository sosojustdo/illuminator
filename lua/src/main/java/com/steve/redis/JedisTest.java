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


        String key = "200:DEFAULT";
        jedis.del(key);
        VendorItemDTO dto = new VendorItemDTO("5500000397","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000398","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000400","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 200.25d);
        dto = new VendorItemDTO("5500000402","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000407","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000405","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000410","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        dto = new VendorItemDTO("5500000412","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 600d);
        dto = new VendorItemDTO("5500000396","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 800d);
        dto = new VendorItemDTO("5500000399","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000403","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000406","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000413","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 700d);
        dto = new VendorItemDTO("5500000415","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 700d);
        jedis.zadd(key, viMap);

        String key1 = "200:APP";
        jedis.del(key1);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000398","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000400","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 200.25d);
        dto = new VendorItemDTO("5500000402","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000407","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000405","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000399","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000403","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000406","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000410","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        jedis.zadd(key1, viMap);

        String key2 = "200:MOBILE_WEB";
        jedis.del(key2);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000398","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000402","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000407","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000405","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000399","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000403","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000406","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        jedis.zadd(key2, viMap);

        String key3 = "200:WEB";
        jedis.del(key3);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000402","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000405","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000399","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000410","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        dto = new VendorItemDTO("5500000412","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 600d);
        jedis.zadd(key3, viMap);

        String key4 = "201:DEFAULT";
        jedis.del(key4);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000501","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 200d);
        dto = new VendorItemDTO("5500000502","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000507","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000509","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000510","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        dto = new VendorItemDTO("5500000512","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 600d);
        dto = new VendorItemDTO("5500000516","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 700d);
        dto = new VendorItemDTO("5500000515","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 700d);
        dto = new VendorItemDTO("5500000599","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000503","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000508","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        jedis.zadd(key4, viMap);

        String key5 = "201:APP";
        jedis.del(key5);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000501","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 200d);
        dto = new VendorItemDTO("5500000502","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000507","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000509","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000503","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000508","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000510","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        dto = new VendorItemDTO("5500000512","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 600d);
        dto = new VendorItemDTO("5500000515","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 700d);
        jedis.zadd(key5, viMap);

        String key6 = "201:WEB";
        jedis.del(key6);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000501","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 200d);
        dto = new VendorItemDTO("5500000502","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000507","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000509","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000599","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000508","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000510","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        dto = new VendorItemDTO("5500000512","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 600d);
        jedis.zadd(key6, viMap);

        String key7 = "201:MOBILE_WEB";
        jedis.del(key7);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000501","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 200d);
        dto = new VendorItemDTO("5500000502","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000599","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000510","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        dto = new VendorItemDTO("5500000512","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 600d);
        dto = new VendorItemDTO("5500000516","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 700d);
        jedis.zadd(key7, viMap);

        String key8 = "202:DEFAULT";
        jedis.del(key8);
        viMap.clear();
        dto = new VendorItemDTO("5500000605","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000601","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 200d);
        dto = new VendorItemDTO("5500000605","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 200d);
        dto = new VendorItemDTO("5500000699","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000610","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000612","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        dto = new VendorItemDTO("5500000616","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 600d);
        jedis.zadd(key8, viMap);

        String key9 = "202:APP";
        jedis.del(key9);
        viMap.clear();
        dto = new VendorItemDTO("5500000605","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000601","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 200d);
        dto = new VendorItemDTO("5500000699","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000612","A00010030", false);
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000616","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        jedis.zadd(key9, viMap);

        String key10 = "200:DEFAULT:TOP";
        jedis.del(key10);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000398","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key10, viMap);

        String key11 = "200:APP:TOP";
        jedis.del(key11);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000398","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key11, viMap);

        String key12 = "200:MOBILE_WEB:TOP";
        jedis.del(key12);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000398","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key12, viMap);

        String key13 = "200:WEB:TOP";
        jedis.del(key13);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key13, viMap);

        String key14 = "201:DEFAULT:TOP";
        jedis.del(key14);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key14, viMap);

        String key15 = "201:APP:TOP";
        jedis.del(key15);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key15, viMap);

        String key16 = "201:WEB:TOP";
        jedis.del(key16);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key16, viMap);

        String key17 = "201:MOBILE_WEB:TOP";
        jedis.del(key17);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028", false);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key17, viMap);

        String key18 = "202:DEFAULT:TOP";
        jedis.del(key18);
        viMap.clear();
        dto = new VendorItemDTO("5500000605","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key18, viMap);

        String key19 = "202:APP:TOP";
        jedis.del(key19);
        viMap.clear();
        dto = new VendorItemDTO("5500000605","A00010029", true);
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key19, viMap);

        String key20 = "200:DEFAULT:REC";
        jedis.del(key20);
        viMap.clear();
        VendorItemRecommendationDTO rdto = new VendorItemRecommendationDTO("5500000397","it is cheap", "A00010028");
        jedis.set(key20, objectMapper.writeValueAsString(rdto));

        String key21 = "200:APP:REC";
        jedis.del(key21);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000397","it is cheap", "A00010028");
        jedis.set(key21, objectMapper.writeValueAsString(rdto));

        String key22 = "200:WEB:REC";
        jedis.del(key22);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000397","it is cheap", "A00010028");
        jedis.set(key22, objectMapper.writeValueAsString(rdto));

        String key23 = "200:MOBILE_WEB:REC";
        jedis.del(key23);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000410","it is close", "A00010029");
        jedis.set(key23, objectMapper.writeValueAsString(rdto));

        String key24 = "201:DEFAULT:REC";
        jedis.del(key24);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000505","it is cheap", "A00010028");
        jedis.set(key24, objectMapper.writeValueAsString(rdto));

        String key25 = "201:APP:REC";
        jedis.del(key25);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000505","it is cheap", "A00010028");
        jedis.set(key25, objectMapper.writeValueAsString(rdto));

        String key26 = "201:WEB:REC";
        jedis.del(key26);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000502","it is cheap", "A00010029");
        jedis.set(key26, objectMapper.writeValueAsString(rdto));

        String key27 = "201:MOBILE_WEB:REC";
        jedis.del(key27);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000502","it is close", "A00010029");
        jedis.set(key27, objectMapper.writeValueAsString(rdto));

        String key28 = "202:DEFAULT:REC";
        jedis.del(key28);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000601","it is wonderful", "A00010028");
        jedis.set(key28, objectMapper.writeValueAsString(rdto));

        String key29 = "202:APP:REC";
        jedis.del(key29);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000610","it is wonderful", "A00010030");
        jedis.set(key29, objectMapper.writeValueAsString(rdto));

        List<Object> results = pipeline.syncAndReturnAll();

        pool.returnResource(jedis);
    }

}
