package com.steve.spark.sample;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by stevexu on 8/29/17.
 */
public class Example1 {

    public static void main(String args[]) throws InterruptedException {

        SparkConf sparkConf = new SparkConf().setAppName("example1").setMaster("local[*]");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        int numMappers = 100;
        final int numKVPairs = 10000;
        int valSize = 1000;
        int numReducers = 100;

        JavaPairRDD<Integer, byte[]> pairRDD = sparkContext.parallelize(Arrays.asList(IntStream.range(0, numMappers).toArray()), numMappers)
                .flatMapToPair((int[] partition) ->
                {
                    Random random = new Random();
                    List<Tuple2<Integer, byte[]>> list = new ArrayList();
                    for (int i = 0; i < numKVPairs; i++) {
                        byte[] value = new byte[valSize];
                        random.nextBytes(value);
                        int key = random.nextInt(100);
                        list.add(new Tuple2<Integer, byte[]>(key, value));
                    }
                    return list.iterator();
                }).cache();

        System.out.println("final count:"+pairRDD.count());
        System.out.println("final reducecount:"+pairRDD.groupByKey(numReducers).count());

        //Thread.sleep(1000000L);

        sparkContext.stop();
    }
}
