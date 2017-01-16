package com.steve.kafkaresearch.consumer;

import com.steve.kafkaresearch.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by stevexu on 1/16/17.
 */
public class WinnerRankingConsumerDemo {

    private static final Logger logger = LoggerFactory.getLogger(WinnerRankingConsumerDemo.class);

    public static void main(String args[]) throws InterruptedException {
        int numConsumers = 3;
        List<String> topics = Arrays.asList(Constants.RANKINGCHANGETOPIC);
        ExecutorService executor = Executors.newFixedThreadPool(numConsumers);

        final List<WinnerRankingConsumerTask> consumers = new ArrayList<>();
        for (int i = 1; i <= numConsumers; i++) {
            WinnerRankingConsumerTask consumer = new WinnerRankingConsumerTask(i, Constants.GROUPID, topics);
            consumers.add(consumer);
            executor.submit(consumer);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (WinnerRankingConsumerTask consumer : consumers) {
                    consumer.shutdown();
                }
                executor.shutdown();
                try {
                    executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
