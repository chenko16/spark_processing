package ru.mephi.spark.banchmark.config;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KubernetesConfig {

    @Value("${kubernetes.cluster.master:https://127.0.0.1:6443}")
    private String kubernetesMaster;

    @Bean
    public KubernetesClient kubernetesClient() {
        System.out.println(kubernetesMaster);
        return new DefaultKubernetesClient(kubernetesMaster);
    }
}
