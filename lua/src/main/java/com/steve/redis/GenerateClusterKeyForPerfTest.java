package com.steve.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author stevexu
 * @Since 9/5/17
 */
public class GenerateClusterKeyForPerfTest {

    public static void main(String[] args) throws IOException {

        /*ResourceBundle bundle = ResourceBundle.getBundle("redis-cluster");
        if (bundle == null) {
            throw new IllegalArgumentException("[redis-cluster.properties] is not found!");
        }
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxTotal")));
        config.setMinIdle(Integer.valueOf(bundle.getString("redis.pool.minIdle")));
        config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
        config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWait")));
        config.setTestWhileIdle(Boolean.valueOf(bundle.getString("redis.pool.testWhileIdle")));
        config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));

        JedisPool pool = new JedisPool(config, "10.255.255.33", 7000);

        Jedis jedis = pool.getResource();
        List<String> retList = new ArrayList<String>();

        String startCursor = "0";
        for(int i=0;i<=1000;i++){
            ScanResult scanResult = jedis.scan(startCursor, new ScanParams().match("W:*:D").count(1000));
            startCursor = scanResult.getStringCursor();
            List<String> result = scanResult.getResult();
            result = result.stream().map(key->key.substring(2, key.length()-2)).collect(Collectors.toList());
            write(result);
        }
        jedis.close();*/
        combine();
    }

    public static void write(List<String> retList) throws IOException {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            File file = new File("/Users/stevexu/projecttest/combine.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);

            for(String str:retList){
                bw.append(str);
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(bw!=null){
                bw.close();
            }
            if(fw!=null){
                fw.close();
            }
        }
    }

    public static void combine() throws IOException {
        BufferedReader br1 = null;
        FileReader fr1 = null;
        BufferedReader br2 = null;
        FileReader fr2 = null;
        BufferedReader br3 = null;
        FileReader fr3 = null;

        try {
            File file1 = new File("/Users/stevexu/projecttest/307000.txt");
            File file2 = new File("/Users/stevexu/projecttest/327000.txt");
            File file3 = new File("/Users/stevexu/projecttest/337000.txt");

            fr1 = new FileReader(file1.getAbsoluteFile());
            br1 = new BufferedReader(fr1);
            fr2 = new FileReader(file2.getAbsoluteFile());
            br2 = new BufferedReader(fr2);
            fr3 = new FileReader(file3.getAbsoluteFile());
            br3 = new BufferedReader(fr3);
            List<String> list1 = new ArrayList<>();
            List<String> list2 = new ArrayList<>();
            List<String> list3 = new ArrayList<>();
            String str;
            while ((str = br1.readLine()) != null) {
                list1.add(str);
            }
            while ((str = br2.readLine()) != null) {
                list2.add(str);
            }
            while ((str = br3.readLine()) != null) {
                list3.add(str);
            }

            List<String> lines = new ArrayList<>();
            for(int i=0;i<95000; i++){
                List<String> sub = new ArrayList<>();
                sub.addAll(list1.subList(i*2, (i+1)*2));
                sub.addAll(list2.subList(i*2, (i+1)*2));
                sub.addAll(list3.subList(i*2, (i+1)*2));
                String strToWrite = sub.stream().collect(Collectors.joining(","));
                lines.add(strToWrite);
            }

            write(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {

        }
    }


}
