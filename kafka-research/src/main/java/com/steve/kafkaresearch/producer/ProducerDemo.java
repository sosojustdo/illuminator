package com.steve.kafkaresearch.producer;

import java.math.BigDecimal;
import java.util.Properties;

import com.steve.kafkaresearch.constants.Constants;
import com.steve.kafkaresearch.partitioner.HashPartitioner;
import com.steve.kafkaresearch.pojo.VendorItemDTO;
import com.steve.kafkaresearch.serialize.CustomDeserializer;
import com.steve.kafkaresearch.serialize.CustomSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;


public class ProducerDemo {


    public static void main(String[] args) throws Exception {
        Producer<String, VendorItemDTO> producer = initProducer();
        sendOne(producer, Constants.TOPIC);
    }

    private static Producer<String, VendorItemDTO> initProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BROKER_LIST);
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomSerializer.class.getName());
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, HashPartitioner.class.getName());

        KafkaProducer kafkaProducer = new KafkaProducer<String, VendorItemDTO>(props);
        return kafkaProducer;
    }

    public static void sendOne(Producer<String, VendorItemDTO> producer, String topic) throws InterruptedException {
        for(int i=1;i<=100;i++){
            ProducerRecord<String, VendorItemDTO> message = new ProducerRecord<>(topic, String.valueOf(i), new VendorItemDTO(55000000+Long.valueOf(i),Long.valueOf(i),new BigDecimal(i/3).doubleValue()));
            producer.send(message);
        }
        producer.close();
    }

}
