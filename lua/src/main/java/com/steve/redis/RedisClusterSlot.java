package com.steve.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;
import redis.clients.util.JedisClusterCRC16;

import java.util.*;

/**
 * Created by stevexu on 7/27/17.
 */
public class RedisClusterSlot {

    public static void main(String[] args) throws JsonProcessingException {
        ResourceBundle bundle = ResourceBundle.getBundle("redis-cluster");
        if (bundle == null) {
            throw new IllegalArgumentException("[redis-cluster.properties] is not found!");
        }
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxTotal")));
        config.setMinIdle(Integer.valueOf(bundle.getString("redis.pool.minIdle")));
        config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
        config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWait")));
        config.setTestWhileIdle(Boolean.valueOf(bundle.getString("redis.pool.testWhileIdle")));
        config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));

        Set<HostAndPort> hostAndPortSet = new HashSet<>();
        hostAndPortSet.add(new HostAndPort("127.0.0.1", 7000));
        hostAndPortSet.add(new HostAndPort("127.0.0.1", 7001));
        hostAndPortSet.add(new HostAndPort("127.0.0.1", 7002));
        hostAndPortSet.add(new HostAndPort("127.0.0.1", 7003));
        hostAndPortSet.add(new HostAndPort("127.0.0.1", 7004));
        hostAndPortSet.add(new HostAndPort("127.0.0.1", 7005));
        hostAndPortSet.add(new HostAndPort("127.0.0.1", 7006));
        hostAndPortSet.add(new HostAndPort("127.0.0.1", 7007));

        JedisCluster jedisCluster = new JedisCluster(hostAndPortSet, 3000, 5, config);

        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 100000; i <= 1000000; i++ ) {
            Map<String, Double> viMap = new HashMap<String, Double>();
            for (int j = 1; j <= 3; j++) {
                VendorItemDTO dto = new VendorItemDTO(String.valueOf(300000000+i+j),"A00010034");
                viMap.put(objectMapper.writeValueAsString(dto), 700d);
                System.out.println(i+ " is mapped to: "+JedisClusterCRC16.getSlot("W:" + i + ":D"));
                jedisCluster.zadd("W:" + i + ":D", viMap);
            }
        }

        /*for (int i = 200000; i <= 200010; i++ ) {
            Map<String, Double> viMap = new HashMap<String, Double>();
            for (int j = 1; j <= 3; j++) {
                VendorItemDTO dto = new VendorItemDTO(String.valueOf(300000000+i+j),"A00010034");
                viMap.put(objectMapper.writeValueAsString(dto), 700d);
                System.out.println(i+ " is mapped to: "+JedisClusterCRC16.getSlot("W:" + i + ":D"));
            }
        }*/

    }

}
