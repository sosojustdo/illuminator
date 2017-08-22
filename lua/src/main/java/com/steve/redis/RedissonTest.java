package com.steve.redis;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;

/**
 * Created by stevexu on 8/12/17.
 */
public class RedissonTest {

    public static void main(String args[]){
        Config config = new Config();
        config.useClusterServers()
                .setReadMode(ReadMode.SLAVE)
                .addNodeAddress("127.0.0.1:7000");

        Redisson redisson = (Redisson) Redisson.create(config);
    }

}
