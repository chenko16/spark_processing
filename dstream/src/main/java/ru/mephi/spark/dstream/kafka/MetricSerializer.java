package ru.mephi.spark.dstream.kafka;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.codehaus.jackson.map.ObjectMapper;
import ru.mephi.spark.dstream.dto.MetricDto;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

public class MetricSerializer implements Serializer<MetricDto> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String s, MetricDto metricDto) {
        return serialize(s, null, metricDto);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, MetricDto data) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        byte[] value = null;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(dateFormat);

        try {
            value = objectMapper.writeValueAsBytes(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }

    @Override
    public void close() {

    }
}
