package com.steve.redis;

/**
 * @author stevexu
 * @Since 9/7/17
 */
import redis.clients.jedis.JedisCluster;


public class RedisClusterSubThread extends Thread {
    private final JedisCluster jedisCluster;
    private final RedisClusterSubscribe subscriber = new RedisClusterSubscribe();

    private final String channel = "mychannel";

    public RedisClusterSubThread(JedisCluster jedisCluster) {
        super("SubThread");
        this.jedisCluster = jedisCluster;
    }

    @Override
    public void run() {
        System.out.println(String.format("subscribe redis, channel %s, thread will be blocked", channel));
        try {
            jedisCluster.subscribe(subscriber, channel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
