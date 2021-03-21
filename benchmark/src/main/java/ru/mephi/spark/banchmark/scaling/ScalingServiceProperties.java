package ru.mephi.spark.banchmark.scaling;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "scaling.service")
public class ScalingServiceProperties {

    private String host;
    private Integer port;

}
