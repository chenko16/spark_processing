package ru.mephi.spark.banchmark.cache;

import ru.mephi.spark.banchmark.dto.MetricDto;

import java.util.Date;

public interface CacheService {

    void addSourceValue(MetricDto metric);
    void addResultValue(MetricDto metric);
    Long checkLatency(Long id, Date time);
    void addLatencyValue(Long id, Date time);
    Long getAverageLatency();

}
