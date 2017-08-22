package com.steve;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by stevexu on 5/3/17.
 */
public class WorkerPool
{

    private static final long TEST_TIMES = 10;
    private static long maxTime, minTime, totalTime, avgTime;
    private static final long ITERATIONS = 5000;
    private static AtomicInteger handleCount = new AtomicInteger(0);

    private static int numOfWorkers = 10;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();

        // RingBuffer生产工厂,初始化RingBuffer的时候使用
        EventFactory<ValueEvent> factory = new EventFactory<ValueEvent>() {
            @Override
            public ValueEvent newInstance() {
                return new ValueEvent();
            }
        };
        // Preallocate RingBuffer with 1024 ValueEvents
        Disruptor<ValueEvent> disruptor = new Disruptor<ValueEvent>(factory, 65536, exec);

        EventHandler<ValueEvent> handlers[] = new EventHandler[10];
        for(int i=0;i<handlers.length;i++){
            final EventHandler<ValueEvent> handler = new EventHandler<ValueEvent>() {
                // event will eventually be recycled by the Disruptor after it wraps
                public void onEvent(final ValueEvent event, final long sequence, final boolean endOfBatch) throws Exception {
                    //do something
                    handleCount.incrementAndGet();
                }
            };
            handlers[i] = handler;
        }
        // Build dependency graph
        disruptor.handleEventsWith(handlers);

        RingBuffer<ValueEvent> ringBuffer = disruptor.start();

        for (int test=0; test < TEST_TIMES; test++) {
            final long tt = System.currentTimeMillis();
            System.out.println("test " + test + " start " + tt);
            for (long i = 0; i < ITERATIONS; i++) {
                //            String uuid = UUID.randomUUID().toString();
                // Two phase commit. Grab one of the 1024 slots
                //reuse the object for gc but reset value
                long seq = ringBuffer.next();
                ValueEvent valueEvent = ringBuffer.get(seq);
                //            valueEvent.setValue(uuid);
                valueEvent.setValue(i);
                //            System.out.println("publishing " + seq + ": " + uuid);
                ringBuffer.publish(seq);
            }

            //wait until all processed
            //        final long expectedSequence = ringBuffer.getCursor();
            //        while (handleCount < expectedSequence) { }
            while (handleCount.get() < ITERATIONS * (test+1)) {Thread.yield(); }

            long ee = System.currentTimeMillis();
            System.out.println("end " + ee);
            long dd = ee - tt;
            long opsPerSecond = (ITERATIONS * 1000L) / dd;
            System.out.printf("time: %d, op/s: %d, handled: %d", dd, opsPerSecond, handleCount.get());
            System.out.println();

            Thread.sleep(2000);

            if (minTime == 0) minTime = dd;
            else if (dd < minTime) minTime = dd;
            if (dd > maxTime) maxTime = dd;
            totalTime += dd;
        }

        disruptor.shutdown();
        exec.shutdown();

        System.out.printf("minTime: %d, maxTime: %d, avgTime: %d", minTime, maxTime, (totalTime / TEST_TIMES));
    }

}
