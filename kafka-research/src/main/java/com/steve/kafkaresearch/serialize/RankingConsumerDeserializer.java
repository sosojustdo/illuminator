package com.steve.kafkaresearch.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steve.kafkaresearch.pojo.WinnerRankingDto;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

/**
 * Created by stevexu on 1/16/17.
 */
public class RankingConsumerDeserializer implements Deserializer<WinnerRankingDto> {


    private static final String ENCODING = "UTF8";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        assert isKey;
    }

    @Override
    public WinnerRankingDto deserialize(String s, byte[] bytes) {
        WinnerRankingDto winnerRankingDto = null;
        try {
            winnerRankingDto = mapper.readValue(bytes, WinnerRankingDto.class);
        } catch (IOException e) {
            throw new SerializationException("Error when json processing byte[] to EventKey", e);
        }
        return winnerRankingDto;
    }


    @Override
    public void close() {
    }
}
