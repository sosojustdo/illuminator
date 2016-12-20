package com.steve.kafkaresearch.consumer;

import com.steve.kafkaresearch.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by stevexu on 12/12/16.
 */
public class ConsumerDemo {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerDemo.class);

    public static Map<String, Integer> finishedSummary = new ConcurrentHashMap<>();

    public static void main(String args[]) throws InterruptedException {
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
                Map<String, Integer> finalSummary = finishedSummary.entrySet().stream().filter(s->s.getValue()>1).collect(Collectors.toMap(s->s.getKey(),s->s.getValue()));
                finalSummary.entrySet().stream().forEach(p->
                    logger.info(p.getKey()+":"+p.getValue())
                );
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
