package com.steve;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by stevexu on 5/3/17.
 */
public class BlockingQueueExample {

    private static final long TEST_TIMES = 10;
    private static long maxTime, minTime, totalTime, avgTime;
    private static final long ITERATIONS = 5000;
    private static long handleCount = 0;

    public static void main(String[] args) throws Exception {
        BlockingQueueExample bqe = new BlockingQueueExample();

        BlockingQueue queue = new ArrayBlockingQueue(1024);

        Consumer consumer = bqe.new Consumer(queue);
        new Thread(consumer).start();

        for (int test=0; test < TEST_TIMES; test++) {
            handleCount = 0;

            long tt = System.currentTimeMillis();
            System.out.println("test " + test + " start " + tt);
            for (long n = 0; n < ITERATIONS; n++) {
                queue.put(n);
            }

            while (handleCount < ITERATIONS) {Thread.yield(); }

            long ee = System.currentTimeMillis();
            System.out.println("end " + ee);
            long dd = ee - tt;
            long opsPerSecond = (ITERATIONS * 1000L) / dd;
            System.out.printf("time: %d, op/s: %d, handled: %d", dd, opsPerSecond, handleCount);
            System.out.println();

            Thread.sleep(2000);

            if (minTime == 0) minTime = dd;
            else if (dd < minTime) minTime = dd;
            if (dd > maxTime) maxTime = dd;
            totalTime += dd;
        }

        queue.put("Done");

        System.out.printf("minTime: %d, maxTime: %d, avgTime: %d", minTime, maxTime, (totalTime / TEST_TIMES));

    }

    public class Producer implements Runnable {

        protected BlockingQueue queue = null;

        public Producer(BlockingQueue queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                for (long n = 0; n < 20; n++) {
                    queue.put(n);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class Consumer implements Runnable {

        protected BlockingQueue queue = null;

        public Consumer(BlockingQueue queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                Object msg = null;
                while (!"Done".equals((msg = queue.take()))) {
//                  System.out.println(msg);
                    handleCount++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
