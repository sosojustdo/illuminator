package com.steve.redis;

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

        String key = "200,DEFAULT";
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

        String key4 = "201,DEFAULT";
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

        String key8 = "202,DEFAULT";
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

        String key10 = "5500000397";
        String key11 = "5500000398";
        String key12 = "5500000400";
        String key13 = "5500000402";
        String key14 = "5500000407";
        String key15 = "5500000405";
        String key16 = "5500000399";
        String key17 = "5500000403";
        String key18 = "5500000406";
        String key19 = "5500000410";
        String key20 = "5500000412";
        String key21 = "5500000413";
        String key22 = "5500000415";
        String key23 = "5500000396";
        String key24 = "5500000505";
        String key25 = "5500000501";
        String key26 = "5500000502";
        String key27 = "5500000507";
        String key28 = "5500000505";
        String key29 = "5500000599";
        String key30 = "5500000503";
        String key31 = "5500000508";
        String key32 = "5500000510";
        String key33 = "5500000512";
        String key34 = "5500000516";
        String key35 = "5500000515";
        String key36 = "5500000605";
        String key37 = "5500000601";
        String key38 = "5500000605";
        String key39 = "5500000699";
        String key40 = "5500000610";
        String key41 = "5500000612";
        String key42 = "5500000616";

        Pipeline pipeline = jedis.pipelined();
        pipeline.hset(key10, "vendorId", "A00010028");
        pipeline.hset(key10, "isCRV", "false");
        pipeline.hset(key11, "vendorId", "A00010028");
        pipeline.hset(key11, "isCRV", "false");
        pipeline.hset(key12, "vendorId", "A00010029");
        pipeline.hset(key12, "isCRV", "true");
        pipeline.hset(key13, "vendorId", "A00010029");
        pipeline.hset(key13, "isCRV", "true");
        pipeline.hset(key14, "vendorId", "A00010029");
        pipeline.hset(key14, "isCRV", "false");
        pipeline.hset(key15, "vendorId", "A00010028");
        pipeline.hset(key15, "isCRV", "false");
        pipeline.hset(key16, "vendorId", "A00010028");
        pipeline.hset(key16, "isCRV", "false");
        pipeline.hset(key17, "vendorId", "A00010030");
        pipeline.hset(key17, "isCRV", "false");
        pipeline.hset(key18, "vendorId", "A00010030");
        pipeline.hset(key18, "isCRV", "false");
        pipeline.hset(key19, "vendorId", "A00010030");
        pipeline.hset(key19, "isCRV", "false");
        pipeline.hset(key20, "vendorId", "A00010028");
        pipeline.hset(key20, "isCRV", "false");
        pipeline.hset(key21, "vendorId", "A00010028");
        pipeline.hset(key21, "isCRV", "false");
        pipeline.hset(key22, "vendorId", "A00010028");
        pipeline.hset(key22, "isCRV", "false");
        pipeline.hset(key23, "vendorId", "A00010029");
        pipeline.hset(key23, "isCRV", "true");
        pipeline.hset(key24, "vendorId", "A00010029");
        pipeline.hset(key24, "isCRV", "true");
        pipeline.hset(key25, "vendorId", "A00010028");
        pipeline.hset(key25, "isCRV", "false");
        pipeline.hset(key26, "vendorId", "A00010028");
        pipeline.hset(key26, "isCRV", "false");
        pipeline.hset(key27, "vendorId", "A00010029");
        pipeline.hset(key27, "isCRV", "true");
        pipeline.hset(key28, "vendorId", "A00010029");
        pipeline.hset(key28, "isCRV", "true");
        pipeline.hset(key29, "vendorId", "A00010029");
        pipeline.hset(key29, "isCRV", "true");
        pipeline.hset(key30, "vendorId", "A00010029");
        pipeline.hset(key30, "isCRV", "true");
        pipeline.hset(key31, "vendorId", "A00010029");
        pipeline.hset(key31, "isCRV", "true");
        pipeline.hset(key32, "vendorId", "A00010029");
        pipeline.hset(key32, "isCRV", "true");
        pipeline.hset(key33, "vendorId", "A00010029");
        pipeline.hset(key33, "isCRV", "true");
        pipeline.hset(key34, "vendorId", "A00010029");
        pipeline.hset(key34, "isCRV", "true");
        pipeline.hset(key35, "vendorId", "A00010028");
        pipeline.hset(key35, "isCRV", "false");
        pipeline.hset(key36, "vendorId", "A00010028");
        pipeline.hset(key36, "isCRV", "false");
        pipeline.hset(key37, "vendorId", "A00010029");
        pipeline.hset(key37, "isCRV", "true");
        pipeline.hset(key38, "vendorId", "A00010030");
        pipeline.hset(key38, "isCRV", "false");
        pipeline.hset(key39, "vendorId", "A00010030");
        pipeline.hset(key39, "isCRV", "false");
        pipeline.hset(key40, "vendorId", "A00010029");
        pipeline.hset(key40, "isCRV", "true");
        pipeline.hset(key41, "vendorId", "A00010029");
        pipeline.hset(key41, "isCRV", "true");
        pipeline.hset(key42, "vendorId", "A00010029");
        pipeline.hset(key42, "isCRV", "true");
        List<Object> results = pipeline.syncAndReturnAll();

        pool.returnResource(jedis);
    }

}
