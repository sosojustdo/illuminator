package com.steve.kafkaresearch.serialize;

import com.coupang.buybox.adapter.avro.BuyboxAvroObjectSerializer;
import com.coupang.buybox.adapter.v1.winner.WinnerRankingDto;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Created by stevexu on 1/17/17.
 */
public class RankingConsumerSerializer implements Serializer<WinnerRankingDto> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        assert isKey;
    }

    @Override
    public byte[] serialize(String s, WinnerRankingDto winnerRankingDto) {
        return BuyboxAvroObjectSerializer.serializeWinnerRankingToByteArray(winnerRankingDto);
    }


    @Override
    public void close() {
    }
}
