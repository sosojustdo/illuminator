package com.steve.kafkaresearch.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.steve.kafkaresearch.pojo.VendorItemDTO;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by stevexu on 12/12/16.
 */
public class CustomSerializer implements Serializer<VendorItemDTO> {

    private static final String ENCODING = "UTF8";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        assert isKey;
    }

    @Override
    public byte[] serialize(String topic, VendorItemDTO data) {
        if (data == null) {
            return null;
        }
        try {
            final String json = mapper.writeValueAsString(data);
            return json.getBytes(ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("Error when serializing string to byte[] due to unsupported encoding " + ENCODING, e);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error when json processing EventKey to byte[]", e);
        }
    }

    @Override
    public void close() {
    }
}
