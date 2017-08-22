package com.steve.spark.sample;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by stevexu on 8/2/17.
 */
public class WorkCount {

    private static final Pattern SPACE = Pattern.compile(" ");

    public static void main(String[] args) throws Exception {

//        if (args.length < 1) {
//            System.err.println("Usage: JavaWordCount <file>");
//            System.exit(1);
//        }


        //创建一个RDD对象
        SparkConf conf = new SparkConf().setAppName("Simple").setMaster("local");

        //创建spark上下文对象，是数据的入口
        JavaSparkContext spark = new JavaSparkContext(conf);

        //获取数据源
        JavaRDD<String> lines = spark.textFile("hdfs://localhost:8020/data/wordcount");

        /**
         * 对于从数据源得到的DStream，用户可以在其基础上进行各种操作，
         * 对于当前时间窗口内从数据源得到的数据首先进行分割，
         * 然后利用Map和ReduceByKey方法进行计算，当然最后还有使用print()方法输出结果；
         */
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) {
                return Arrays.asList(SPACE.split(s)).iterator();
            }
        });


        //使用RDD的map和reduce方法进行计算
        JavaPairRDD<String, Integer> ones = words.mapToPair(
                new PairFunction<String, String, Integer>() {
                    @Override
                    public Tuple2<String, Integer> call(String s) {
                        return new Tuple2<>(s, 1);
                    }
                });


        JavaPairRDD<String, Integer> counts = ones.reduceByKey(
                new Function2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer i1, Integer i2) {
                        return i1 + i2;
                    }
                });

        List<Tuple2<String, Integer>> output = counts.collect();
        for (Tuple2<?, ?> tuple : output) {
            //输出计算结果
            System.out.println(tuple._1() + ": " + tuple._2());
        }


        spark.stop();
    }


}
