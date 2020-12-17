package ru.mephi.spark.rdd.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mephi.spark.rdd.dto.MetricDto;
import ru.mephi.spark.rdd.jpa.MetricEntity;
import ru.mephi.spark.rdd.jpa.MetricRepository;

@Service
public class MetricKafkaListener {

    @Autowired
    private MetricRepository metricRepository;


    @KafkaListener(topics = {"#{@topicName}"}, containerFactory = "metricKafkaListenerContainerFactory")
    public void consume(MetricDto dto) {
        MetricEntity metricEntity = new MetricEntity(dto.getId(), dto.getTime(), dto.getValue());
        metricRepository.save(metricEntity);
    }
}
