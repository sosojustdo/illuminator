package com.steve.kafkaresearch.producer;

import com.steve.kafkaresearch.constants.Constants;
import com.steve.kafkaresearch.pojo.VendorItemDTO;
import com.steve.kafkaresearch.pojo.VendorItemRank;
import com.steve.kafkaresearch.pojo.WinnerRankingDto;
import com.steve.kafkaresearch.serialize.RankingConsumerSerializer;
import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by stevexu on 1/16/17.
 */
public class WinnerRankingProducer {

    static Producer<String, WinnerRankingDto> producer;

    private static final Logger logger = LoggerFactory.getLogger(ProducerDemo.class);

    public static void main(String[] args) throws Exception {
        initProducer();
        sendBatch(producer, Constants.RANKINGCHANGETOPIC);
    }

    public static void initProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BROKER_LIST);
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, RankingConsumerSerializer.class.getName());

        KafkaProducer kafkaProducer = new KafkaProducer<String, VendorItemDTO>(props);
        producer = kafkaProducer;
    }

    public static void sendBatch(Producer<String, WinnerRankingDto> producer, String topic) throws InterruptedException {
        for(int i=200;i<205;i++){
            List<VendorItemRank> ranks = new ArrayList<>();
            ranks.add(new VendorItemRank(Long.valueOf(i), "Vendor100", 0));
            ranks.add(new VendorItemRank(Long.valueOf(i), "Vendor200", 1));
            ranks.add(new VendorItemRank(Long.valueOf(i), "Vendor300", 2));
            ranks.add(new VendorItemRank(Long.valueOf(i), "Vendor300", 3));
            ranks.add(new VendorItemRank(Long.valueOf(i), "Vendor300", 4));
            ranks.add(new VendorItemRank(Long.valueOf(i), "Vendor200", 5));
            ranks.add(new VendorItemRank(Long.valueOf(i), "Vendor100", 6));
            ranks.add(new VendorItemRank(Long.valueOf(i), "Vendor400", 7));
            ranks.add(new VendorItemRank(Long.valueOf(i), "Vendor500", 8));
            ranks.add(new VendorItemRank(Long.valueOf(i), "Vendor500", 9));
            ProducerRecord<String, WinnerRankingDto> message = new ProducerRecord<>(topic, String.valueOf(i), new WinnerRankingDto(Long.valueOf(i), "APP", new Date().getTime(), ranks));
            producer.send(message, (RecordMetadata recordMetadata, Exception e)->{
                if(e!=null){
                    logger.error("error while send to kafka, itemid:"+message.value().getItemId(),e);
                }
            });
        }
        producer.close();
    }

}
