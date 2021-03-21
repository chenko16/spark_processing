package ru.mephi.spark.banchmark.cache;

import org.springframework.stereotype.Service;
import ru.mephi.spark.banchmark.dto.MetricDto;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheServiceImpl implements CacheService {

    private Map<Long, Map<Date, Date>> sourceMap = new ConcurrentHashMap<>();
    private Map<Long, Map<Date, Date>> resultMap = new ConcurrentHashMap<>();
    private Map<LatencyKey, Long> latencyMap;

    @Override
    public void addSourceValue(MetricDto metric) {
        Map<Date, Date> group = sourceMap.computeIfAbsent(metric.getId(), k -> new ConcurrentHashMap<>());
        group.computeIfAbsent(metric.getTime(), k -> new Date());
    }

    @Override
    public void addResultValue(MetricDto metric) {
        Map<Date, Date> group = resultMap.computeIfAbsent(metric.getId(), k -> new ConcurrentHashMap<>());
        group.computeIfAbsent(metric.getTime(), k -> new Date());
    }

    @Override
    public Long checkLatency(Long id, Date time) {
        Map<Date, Date> sourceGroup = sourceMap.get(id);
        Map<Date, Date> resultGroup = resultMap.get(id);
        if(sourceGroup != null && resultGroup != null) {
            Date sourceDate = sourceGroup.get(time);
            Date resultDate = resultGroup.get(time);
            if(sourceDate != null && resultDate != null) {
                return resultDate.getTime() - sourceDate.getTime();
            }

            return null;
        }

        return null;
    }

    @Override
    public void addLatencyValue(Long id, Date time) {
        Long latency = this.checkLatency(id, time);
        if(latency < 0) {
            return;
        }

        Integer order = 0;
        Long lastSavedLatency = null;
        do {
            LatencyKey key = new LatencyKey(id, time, order);
            lastSavedLatency = latencyMap.get(key);
            if(lastSavedLatency != null) {
                order++;
            }
        } while (lastSavedLatency != null);

        LatencyKey key = new LatencyKey(id, time, order);
        latencyMap.put(key, latency);
    }

    @Override
    public Long getAverageLatency(Long id, Date time) {
        Integer order = 0;
        Long latency = null;
        long sum = 0L;
        int count = 0;

        do {
            LatencyKey key = new LatencyKey(id, time, order);
            latency = latencyMap.get(key);
            if(latency != null) {
                sum += latency;
                count++;
            }
        } while (latency != null);

        return count != 0 ? sum / count : 0;
    }
}
