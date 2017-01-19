package com.steve.kafkaresearch.consumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by stevexu on 1/16/17.
 */
public class WinnerRankingFakeConsumer {

    public static void main(String args[]) throws InterruptedException, IOException {
        int numConsumers = 3;
        InputStream input = WinnerRankingFakeConsumer.class.getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();
        properties.load(input);

        List<String> topics = Arrays.asList(properties.getProperty("topic"));
        ExecutorService executor = Executors.newFixedThreadPool(numConsumers);

        final List<WinnerRankingConsumerTask> consumers = new ArrayList<>();
        for (int i = 1; i <= numConsumers; i++) {
            WinnerRankingConsumerTask consumer = new WinnerRankingConsumerTask(i, (String)properties.getProperty("groupId"), topics, (String)properties.get("hosts"));
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
