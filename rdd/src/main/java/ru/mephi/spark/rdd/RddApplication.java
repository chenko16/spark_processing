package ru.mephi.spark.rdd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class RddApplication {

    public static void main(String[] args) {
        SpringApplication.run(RddApplication.class, args);
    }
}
