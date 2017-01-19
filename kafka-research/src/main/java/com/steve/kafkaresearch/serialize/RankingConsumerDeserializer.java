package com.steve.kafkaresearch.serialize;

import com.coupang.buybox.adapter.avro.BuyboxAvroObjectSerializer;
import com.coupang.buybox.adapter.v1.winner.WinnerRankingDto;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Created by stevexu on 1/17/17.
 */
public class RankingConsumerDeserializer implements Deserializer<WinnerRankingDto> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        assert isKey;
    }

    @Override
    public WinnerRankingDto deserialize(String s, byte[] bytes) {
        return BuyboxAvroObjectSerializer.deserializeToWinnerRanking(bytes);
    }

    @Override
    public void close() {
    }
}
