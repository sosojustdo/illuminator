package com.steve.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.*;

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


        String key = "WIN:200:DEF";
        jedis.del(key);
        VendorItemDTO dto = new VendorItemDTO("5500000397","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000398","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000400","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 200.25d);
        dto = new VendorItemDTO("5500000402","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000407","A00010031");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000405","A00010032");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000410","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        dto = new VendorItemDTO("5500000412","A00038294");
        viMap.put(objectMapper.writeValueAsString(dto), 600d);
        dto = new VendorItemDTO("5500000396","A00045899");
        viMap.put(objectMapper.writeValueAsString(dto), 800d);
        dto = new VendorItemDTO("5500000399","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000403","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000406","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000413","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 700d);
        dto = new VendorItemDTO("5500000415","A00010034");
        viMap.put(objectMapper.writeValueAsString(dto), 700d);
        jedis.zadd(key, viMap);

        String key1 = "WIN:200:APP";
        jedis.del(key1);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000398","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000400","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 200.25d);
        dto = new VendorItemDTO("5500000402","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000407","A00010031");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000405","A00010032");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000399","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000403","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000406","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000410","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        jedis.zadd(key1, viMap);

        String key2 = "WIN:200:MOBILE_WEB";
        jedis.del(key2);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000398","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000402","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000407","A00010031");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000405","A00010032");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000399","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000403","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000406","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        jedis.zadd(key2, viMap);

        String key3 = "WIN:200:WEB";
        jedis.del(key3);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000402","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000405","A00010032");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000399","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 400.5d);
        dto = new VendorItemDTO("5500000410","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        dto = new VendorItemDTO("5500000412","A00038294");
        viMap.put(objectMapper.writeValueAsString(dto), 600d);
        jedis.zadd(key3, viMap);

        String key4 = "WIN:201:DEF";
        jedis.del(key4);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000501","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 200d);
        dto = new VendorItemDTO("5500000502","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000507","A00010031");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000509","A00010032");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000510","A00038294");
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        dto = new VendorItemDTO("5500000512","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 600d);
        dto = new VendorItemDTO("5500000516","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 700d);
        dto = new VendorItemDTO("5500000515","A00010034");
        viMap.put(objectMapper.writeValueAsString(dto), 700d);
        dto = new VendorItemDTO("5500000599","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000503","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000508","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        jedis.zadd(key4, viMap);

        String key5 = "WIN:201:APP";
        jedis.del(key5);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000501","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 200d);
        dto = new VendorItemDTO("5500000502","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000507","A00010031");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000509","A00010032");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000503","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000508","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000510","A00038294");
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        dto = new VendorItemDTO("5500000512","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 600d);
        dto = new VendorItemDTO("5500000515","A00010034");
        viMap.put(objectMapper.writeValueAsString(dto), 700d);
        jedis.zadd(key5, viMap);

        String key6 = "WIN:201:WEB";
        jedis.del(key6);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000501","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 200d);
        dto = new VendorItemDTO("5500000502","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000507","A00010031");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000509","A00010032");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000599","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000508","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000510","A00038294");
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        dto = new VendorItemDTO("5500000512","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 600d);
        jedis.zadd(key6, viMap);

        String key7 = "WIN:201:MOBILE_WEB";
        jedis.del(key7);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000501","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 200d);
        dto = new VendorItemDTO("5500000502","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000599","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000510","A00038294");
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        dto = new VendorItemDTO("5500000512","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 600d);
        dto = new VendorItemDTO("5500000516","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 700d);
        jedis.zadd(key7, viMap);

        String key8 = "WIN:202:DEF";
        jedis.del(key8);
        viMap.clear();
        dto = new VendorItemDTO("5500000605","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000601","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 200d);
        dto = new VendorItemDTO("5500000605","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 200d);
        dto = new VendorItemDTO("5500000699","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000610","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000612","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        dto = new VendorItemDTO("5500000616","A00045899");
        viMap.put(objectMapper.writeValueAsString(dto), 600d);
        jedis.zadd(key8, viMap);

        String key9 = "WIN:202:APP";
        jedis.del(key9);
        viMap.clear();
        dto = new VendorItemDTO("5500000605","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000601","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 200d);
        dto = new VendorItemDTO("5500000699","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 300d);
        dto = new VendorItemDTO("5500000612","A00010030");
        viMap.put(objectMapper.writeValueAsString(dto), 400d);
        dto = new VendorItemDTO("5500000616","A00045899");
        viMap.put(objectMapper.writeValueAsString(dto), 500d);
        jedis.zadd(key9, viMap);

        String key10 = "TOP:200:DEF";
        jedis.del(key10);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000398","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key10, viMap);

        String key11 = "TOP:200:APP";
        jedis.del(key11);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000398","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key11, viMap);

        String key12 = "TOP:200:MOBILE_WEB";
        jedis.del(key12);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        dto = new VendorItemDTO("5500000398","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key12, viMap);

        String key13 = "TOP:200:WEB";
        jedis.del(key13);
        viMap.clear();
        dto = new VendorItemDTO("5500000397","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key13, viMap);

        String key14 = "TOP:201:DEF";
        jedis.del(key14);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key14, viMap);

        String key15 = "TOP:201:APP";
        jedis.del(key15);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key15, viMap);

        String key16 = "TOP:201:WEB";
        jedis.del(key16);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key16, viMap);

        String key17 = "TOP:201:MOBILE_WEB";
        jedis.del(key17);
        viMap.clear();
        dto = new VendorItemDTO("5500000505","A00010028");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key17, viMap);

        String key18 = "TOP:202:DEF";
        jedis.del(key18);
        viMap.clear();
        dto = new VendorItemDTO("5500000605","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key18, viMap);

        String key19 = "TOP:202:APP";
        jedis.del(key19);
        viMap.clear();
        dto = new VendorItemDTO("5500000605","A00010029");
        viMap.put(objectMapper.writeValueAsString(dto), 100d);
        jedis.zadd(key19, viMap);

        String key20 = "REC:200:DEF";
        jedis.del(key20);
        viMap.clear();
        VendorItemRecommendationDTO rdto = new VendorItemRecommendationDTO("5500000397","it is cheap", "A00010028");
        jedis.set(key20, objectMapper.writeValueAsString(rdto));

        String key21 = "REC:200:APP";
        jedis.del(key21);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000397","it is cheap", "A00010028");
        jedis.set(key21, objectMapper.writeValueAsString(rdto));

        String key22 = "REC:200:WEB";
        jedis.del(key22);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000397","it is cheap", "A00010028");
        jedis.set(key22, objectMapper.writeValueAsString(rdto));

        String key23 = "REC:200:MOBILE_WEB";
        jedis.del(key23);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000410","it is close", "A00010029");
        jedis.set(key23, objectMapper.writeValueAsString(rdto));

        String key24 = "REC:201:DEF";
        jedis.del(key24);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000505","it is cheap", "A00010028");
        jedis.set(key24, objectMapper.writeValueAsString(rdto));

        String key25 = "REC:201:APP";
        jedis.del(key25);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000505","it is cheap", "A00010028");
        jedis.set(key25, objectMapper.writeValueAsString(rdto));

        String key26 = "REC:201:WEB";
        jedis.del(key26);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000502","it is cheap", "A00010029");
        jedis.set(key26, objectMapper.writeValueAsString(rdto));

        String key27 = "REC:201:MOBILE_WEB";
        jedis.del(key27);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000502","it is close", "A00010029");
        jedis.set(key27, objectMapper.writeValueAsString(rdto));

        String key28 = "REC:202:DEF";
        jedis.del(key28);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000601","it is wonderful", "A00010028");
        jedis.set(key28, objectMapper.writeValueAsString(rdto));

        String key29 = "REC:202:APP";
        jedis.del(key29);
        viMap.clear();
        rdto = new VendorItemRecommendationDTO("5500000610","it is wonderful", "A00010030");
        jedis.set(key29, objectMapper.writeValueAsString(rdto));

        List<Object> results = pipeline.syncAndReturnAll();

        pool.returnResource(jedis);
    }

}
