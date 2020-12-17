package ru.mephi.spark.rdd.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

    @Bean
    public SparkConf sparkConf() {
        return new SparkConf()
                .setMaster("local[*]")
                .setAppName("Spark RDD metric aggregator");
    }

    @Bean
    public JavaSparkContext javaSparkContext(SparkConf sparkConf) {
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        sc.setLogLevel("ERROR");
        return sc;
    }
}
