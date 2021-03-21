package ru.mephi.spark.banchmark.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mephi.spark.banchmark.cache.CacheService;
import ru.mephi.spark.banchmark.dto.MetricDto;
import ru.mephi.spark.banchmark.util.DateUtil;

@Service
@RequiredArgsConstructor
public class MetricKafkaListener {

    private final CacheService cacheService;

    @KafkaListener(topics = {"#{@sourceTopic}"}, containerFactory = "metricKafkaListenerContainerFactory")
    public void sourceConsume(MetricDto dto) {
        dto.setTime(DateUtil.roundMinutes(dto.getTime(), 5));
        cacheService.addSourceValue(dto);
    }

    @KafkaListener(topics = {"#{@resultTopic}"}, containerFactory = "metricKafkaListenerContainerFactory")
    public void resultConsume(MetricDto dto) {
        cacheService.addResultValue(dto);
        Long latency = cacheService.checkLatency(dto.getId(), dto.getTime());
        if(latency != null) {
            System.out.println(String.format("Latency for id %d and time %s: %d ms", dto.getId(), dto.getTime().toString(), latency));
        }

    }

}
