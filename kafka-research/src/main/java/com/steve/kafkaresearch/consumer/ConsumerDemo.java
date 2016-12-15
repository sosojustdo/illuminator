package com.steve.kafkaresearch.consumer;

import com.steve.kafkaresearch.constants.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by stevexu on 12/12/16.
 */
public class ConsumerDemo {



    public static void main(String args[]){
        int numConsumers = 3;
        List<String> topics = Arrays.asList(Constants.TOPIC);
        ExecutorService executor = Executors.newFixedThreadPool(numConsumers);

        final List<ConsumerTask> consumers = new ArrayList<>();
        for (int i = 1; i <= numConsumers; i++) {
            ConsumerTask consumer = new ConsumerTask(i, Constants.GROUPID, topics);
            consumers.add(consumer);
            executor.submit(consumer);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (ConsumerTask consumer : consumers) {
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
