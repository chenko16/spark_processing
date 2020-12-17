package ru.mephi.spark.dstream;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import ru.mephi.spark.dstream.dto.MetricDto;
import ru.mephi.spark.dstream.kafka.KafkaProducerFactory;
import ru.mephi.spark.dstream.kafka.MetricDeserializer;
import ru.mephi.spark.dstream.util.DateUtil;
import scala.Tuple2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DStreamApplication {

    private static final String BROKERS = "localhost:9092";

    private static final String INPUT_TOPIC= "spark";

    private static final String OUTPUT_TOPIC= "sparkResult";


    public static void main(String[] args) throws InterruptedException {
        if(args.length < 1) {
            System.out.println("Usage: java -jar jarName windowLength windowUpdatePeriod");
            return;
        }

        Integer windowLength = Integer.parseInt(args[0]);
        Integer windowUpdatePeriod = Integer.parseInt(args[1]);

        SparkConf sparkConf = new SparkConf()
                .setAppName("Spark DStream Application")
                .setMaster("local");

        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));

        SparkSession sparkSession = SparkSession.builder()
                .appName("Spark DStream Application")
                .getOrCreate();
        sparkSession.sparkContext().setLogLevel("ERROR");

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", BROKERS);
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", MetricDeserializer.class);
        kafkaParams.put("group.id", "dstream");
        kafkaParams.put("auto.offset.reset", "latest");

        JavaInputDStream<ConsumerRecord<String, MetricDto>> inputDStream =
                KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(List.of(INPUT_TOPIC), kafkaParams)
                );

        JavaDStream<MetricDto> metricStream = inputDStream
                .map(ConsumerRecord::value);

        JavaDStream<MetricDto> aggregatedStream = metricStream
                .map(metric -> {
                    metric.setTime(DateUtil.roundMinutes(metric.getTime(), windowUpdatePeriod));
                    return metric;
                })
                .mapToPair(metric -> new Tuple2<>(new Tuple2<>(metric.getId(), metric.getTime()), new Tuple2<>(metric.getValue(), 1)))
                .reduceByKeyAndWindow((x, y) -> new Tuple2<>(x._1 + y._1, x._2 + y._2), Durations.minutes(windowLength), Durations.minutes(windowUpdatePeriod))
                .mapValues(value -> value._1 / value._2)
                .map(pair -> new MetricDto(pair._1._1, pair._1._2, pair._2));

//        aggregatedStream.print();

        aggregatedStream
                .foreachRDD(metricRdd -> metricRdd.foreach(metric -> {
                    Producer<String, MetricDto> innerProducer = KafkaProducerFactory.getKafkaProducer();
                    innerProducer.send(new ProducerRecord<>(OUTPUT_TOPIC, metric));
                }));

        streamingContext.start();
        streamingContext.awaitTermination();

    }
}
