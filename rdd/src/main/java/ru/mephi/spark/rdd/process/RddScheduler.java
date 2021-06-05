package ru.mephi.spark.rdd.process;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.mephi.spark.rdd.dto.MetricDto;
import ru.mephi.spark.rdd.jpa.MetricEntity;
import ru.mephi.spark.rdd.jpa.MetricRepository;
import ru.mephi.spark.rdd.jpa.ResultRepository;
import ru.mephi.spark.rdd.util.DateUtil;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class RddScheduler {

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("#{T(java.lang.Integer).parseInt('${rdd.query.window.length}')}")
    private Integer windowLength;

    @Value("#{T(java.lang.Integer).parseInt('${rdd.query.window.update}')}")
    private Integer windowUpdatePeriod;

    @Value("${kafka.producer.topic}")
    private String kafkaTopic;

    @Autowired
    private JavaSparkContext sparkContext;

    @Scheduled(fixedRate = 10)
    public void processRecords() {
        aggregateMetrics();
    }

    @Scheduled(fixedRate = 10)
    private void aggregateMetrics() {
        Calendar date = Calendar.getInstance();
        Date windowStart = DateUtil.getWindowStartMinutes(date, windowLength);
        Date windowEnd = DateUtil.getWindowEndMinutes(date, windowLength);

        List<MetricDto> metrics = metricRepository.findAllByTimeBetween(windowStart, windowEnd).stream()
                .map(entity -> new MetricDto(entity.getGroupId(), entity.getTime(), entity.getValue()))
                .collect(Collectors.toList());

        JavaRDD<MetricDto> metricRdd = sparkContext.parallelize(metrics);

        JavaRDD<MetricDto> aggregatedMetrics = metricRdd
                .map(metric -> {
                    metric.setTime(DateUtil.roundMinutes(metric.getTime(), windowUpdatePeriod));
                    return metric;
                })
                .mapToPair(metric -> new Tuple2<>(new Tuple2<>(metric.getId(), metric.getTime()),
                        new Tuple2<>(metric.getValue(), 1)))
                .reduceByKey((x, y) -> new Tuple2<>(x._1 + y._1, x._2 + y._2))
                .mapValues(value -> (value._1) / value._2)
                .map(pair -> new MetricDto(pair._1._1, pair._1._2, pair._2));

        aggregatedMetrics
                .collect()
                .stream()
                .map(dto -> new MetricDto(dto.getId(), dto.getTime(), dto.getValue()))
                .collect(Collectors.toList())
                .forEach(metric -> kafkaTemplate.send(kafkaTopic, metric));
    }
}
