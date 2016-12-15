package com.steve.kafkaresearch.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.steve.kafkaresearch.pojo.VendorItemDTO;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by stevexu on 12/12/16.
 */
public class CustomDeserializer implements Deserializer<VendorItemDTO> {


    private static final String ENCODING = "UTF8";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        assert isKey;
    }

    @Override
    public VendorItemDTO deserialize(String s, byte[] bytes) {
        VendorItemDTO vendorItemDTO = null;
        try {
            vendorItemDTO = mapper.readValue(bytes, VendorItemDTO.class);
        } catch (IOException e) {
            throw new SerializationException("Error when json processing byte[] to EventKey", e);
        }
        return vendorItemDTO;
    }


    @Override
    public void close() {
    }
}
