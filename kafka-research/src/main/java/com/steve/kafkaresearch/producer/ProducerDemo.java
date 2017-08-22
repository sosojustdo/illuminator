package com.steve.kafkaresearch.producer;

import java.math.BigDecimal;
import java.util.Properties;

import com.steve.kafkaresearch.constants.Constants;
import com.steve.kafkaresearch.partitioner.HashPartitioner;
import com.steve.kafkaresearch.pojo.VendorItemDTO;
import com.steve.kafkaresearch.serialize.CustomSerializer;
import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProducerDemo {

    static Producer<String, VendorItemDTO> producer;

    private static final Logger logger = LoggerFactory.getLogger(ProducerDemo.class);

    public static void main(String[] args) throws Exception {
        initProducer();
        sendBatch(producer, Constants.TOPIC);
    }

    public static void initProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BROKER_LIST);
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomSerializer.class.getName());
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, HashPartitioner.class.getName());

        KafkaProducer kafkaProducer = new KafkaProducer<String, VendorItemDTO>(props);
        producer = kafkaProducer;
    }

    public static void sendBatch(Producer<String, VendorItemDTO> producer, String topic) throws InterruptedException {
        for(int i=1;i<=3000;i++){
            ProducerRecord<String, VendorItemDTO> message = new ProducerRecord<>(topic, String.valueOf(i), new VendorItemDTO(55000000+Long.valueOf(i),Long.valueOf(i),new BigDecimal(i/3).doubleValue()));
            producer.send(message, (RecordMetadata recordMetadata, Exception e)->{
                if(e!=null){
                    logger.error("error while send to kafka, itemid:"+message.value().getItemId(),e);
                }
            });
        }
        producer.close();
    }

    public static void sendOne(String topic, VendorItemDTO vendorItemDTO){
        ProducerRecord<String, VendorItemDTO> message = new ProducerRecord<>(topic, String.valueOf(vendorItemDTO.getItemId()), vendorItemDTO);
        producer.send(message, new Callback(){
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if(e!=null){
                    logger.error("error while send to kafka, itemid:"+vendorItemDTO.getItemId(),e);
                }
            }
        });
    }

}
