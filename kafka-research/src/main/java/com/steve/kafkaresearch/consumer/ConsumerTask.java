package com.steve.kafkaresearch.consumer;

import com.steve.kafkaresearch.constants.Constants;
import com.steve.kafkaresearch.pojo.VendorItemDTO;
import com.steve.kafkaresearch.producer.ProducerDemo;
import com.steve.kafkaresearch.serialize.CustomDeserializer;
import com.steve.kafkaresearch.serialize.CustomSerializer;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ConsumerTask implements Runnable {

    //static private final String ZOOKEEPER = "localhost:2181,localhost:2182,localhost:2183";
    static private final String BROKER_LIST = "127.0.0.1:9093,127.0.0.1:9094,127.0.0.1:9095";

    private static List<String> topics = new ArrayList<>();

    private final Consumer<String, VendorItemDTO> consumer;

    private final int id;

    private static final Logger logger = LoggerFactory.getLogger(ConsumerTask.class);

    public ConsumerTask(int id, String groupId, List<String> topics) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "3000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, "120000");
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 10485760/1000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomDeserializer.class.getName());
        consumer = new KafkaConsumer<>(props);
        ProducerDemo.initProducer();
        this.topics = topics;
        this.id = id;
    }

    private boolean doCommitSync(VendorItemDTO vendorItemDTO) {
        try {
            consumer.commitSync();
            return true;
        } catch (WakeupException e) {
            // we're shutting down, but finish the commit first and then
            // rethrow the exception so that the main loop can exit
            doCommitSync(vendorItemDTO);
            throw e;
        } catch (CommitFailedException e) {
            // the commit failed with an unrecoverable error. if there is any
            // internal state which depended on the commit, you can clean it
            // up here. otherwise it's reasonable to ignore the error and go on
            if(vendorItemDTO!=null && vendorItemDTO.getItemId()!=null){
                logger.error("Commit failed, itemid:"+vendorItemDTO.getItemId()+",will trigger a retry");
                ProducerDemo.sendOne(Constants.TOPIC, vendorItemDTO);
            }
            return false;
        }
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(topics, new ConsumerRebalanceListener() {
                public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                    partitions.forEach(p -> {
                        logger.info("Revoked partition for client: "+id+ " topic:"+p.topic()+","+p.partition());
                    });
                    doCommitSync(null);
                }

                public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                    partitions.forEach(p -> {
                        logger.info("assigned partition for client: "+id+" topic:"+ p.topic()+",partition:"+p.partition());
                    });
                }
            });

            int count = 0;

            while (true) {
                ConsumerRecords<String, VendorItemDTO> records = consumer.poll(1000*10);
                if(records.count()>0){
                    logger.info("pull "+records.count()+" to consume");
                }
                else{
                    logger.warn("there is no records pooling from consumer within 10 seconds");
                }
                for (ConsumerRecord<String, VendorItemDTO> record : records) {
                    if(doCommitSync(record.value())){
                        Map<String, Object> data = new HashMap<>();
                        data.put("partition", record.partition());
                        data.put("offset", record.offset());
                        data.put("vendorItemId", record.value().getVendorItemId());
                        data.put("itemId", record.value().getItemId());
                        data.put("sellerRate", record.value().getNewSellerRate());
                        logger.info(this.id + ": " + data);
                        //consumer.commitAsync();
                        count++;
                    /*while(i<=10000000L){
                        i++;
                    }*/
                        if(count%10000==0){
                            Thread.sleep(40000);
                        }
                    }
                }
                if(records.count()>0){
                    logger.info("Thread "+id+" finished consumes："+count+"");
                }
            }
        } catch (WakeupException e) {
            // ignore, we're closing
            logger.warn("wakeup exception");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unexpected error", e);
        } finally {
            try {
                doCommitSync(null);
            } finally {
                consumer.close();
            }
        }
    }

    public void shutdown() {
        consumer.wakeup();
    }


}