package ru.mephi.spark.dstream.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.mephi.spark.dstream.dto.MetricDto;

import java.util.Properties;

public class KafkaProducerFactory {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    private static Properties getProps() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "dstream");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                MetricSerializer.class.getName());

        return props;
    }

    public static Producer<String, MetricDto> getKafkaProducer() {
        return new KafkaProducer<>(getProps());
    }
}
