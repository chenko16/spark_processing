package ru.mephi.spark.banchmark.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mephi.spark.banchmark.cache.CacheService;
import ru.mephi.spark.banchmark.dto.MetricDto;
import ru.mephi.spark.banchmark.util.DateUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class MetricKafkaListener {

    private final CacheService cacheService;
    @Value("#{T(java.lang.Integer).parseInt('${records.round}")
    private Integer roundValue;

    @KafkaListener(topics = {"#{@sourceTopic}"}, containerFactory = "metricKafkaListenerContainerFactory")
    public void sourceConsume(MetricDto dto) {
        dto.setTime(DateUtil.roundMinutes(dto.getTime(), roundValue));
        cacheService.addSourceValue(dto);
    }

    @KafkaListener(topics = {"#{@resultTopic}"}, containerFactory = "metricKafkaListenerContainerFactory")
    public void resultConsume(MetricDto dto) {
        cacheService.addResultValue(dto);
        Long latency = cacheService.checkLatency(dto.getId(), dto.getTime());
        if(latency != null) {
            log.info(String.format("Latency for id %d and time %s: %d ms", dto.getId(), dto.getTime().toString(), latency));
        }
    }
}
