package com.steve.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RFuture;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSortedSet;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by stevexu on 8/12/17.
 */
public class RedissonTest {

    public static void main(String args[]) throws JsonProcessingException, ExecutionException, InterruptedException {
        Config config = new Config();
        config.useClusterServers()
              .setReadMode(ReadMode.SLAVE)
              .setMasterConnectionPoolSize(500)
              .setIdleConnectionTimeout(60000)
              .addNodeAddress("redis://127.0.0.1:7000", "redis://127.0.0.1:7001", "redis://127.0.0.1:7002", "redis://127.0.0.1:7003",
                              "redis://127.0.0.1:7004", "redis://127.0.0.1:7005", "redis://127.0.0.1:7006", "redis://127.0.0.1:7007"
                      , "redis://127.0.0.1:7008");

        Redisson redisson = (Redisson) Redisson.create(config);
        RScoredSortedSet<VendorItemDTO> sortedSet = redisson.getScoredSortedSet("W:10000001");

       /* Map<VendorItemDTO, Double> map = new HashMap<>();

        map.put(new VendorItemDTO(String.valueOf("300000000"),"A00010034"), 10D);
        map.put(new VendorItemDTO(String.valueOf("300000001"),"A00010034"), 20D);
        map.put(new VendorItemDTO(String.valueOf("300000002"),"A00010034"), 30D);

        sortedSet.addAll(map);*/
/*
        Map<String, Double> map = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();

        map.put(objectMapper.writeValueAsString(new VendorItemDTO(String.valueOf("300000000"),"A00010034")), 10D);
        map.put(objectMapper.writeValueAsString(new VendorItemDTO(String.valueOf("300000001"),"A00010034")), 20D);
        map.put(objectMapper.writeValueAsString(new VendorItemDTO(String.valueOf("300000002"),"A00010034")), 30D);

        sortedSet.addAll(map);*/

        RFuture rFuture = sortedSet.valueRangeAsync(0, -1);

        System.out.println(rFuture.get());
        redisson.shutdown();

    }

}
