package com.steve.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.util.JedisClusterCRC16;

import java.util.*;

/**
 * @author stevexu
 * @Since 9/7/17
 */
public class RedisClusterPubSubTest {

    public static void main(String args[]){

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
        hostAndPortSet.add(new HostAndPort("127.0.0.1", 7008));

        JedisCluster jedisCluster = new JedisCluster(hostAndPortSet, 3000, 5, config);

        RedisClusterSubThread subThread = new RedisClusterSubThread(jedisCluster);
        subThread.start();

        RedisClusterPublisher publisher = new RedisClusterPublisher(jedisCluster);
        publisher.start();
    }

}
