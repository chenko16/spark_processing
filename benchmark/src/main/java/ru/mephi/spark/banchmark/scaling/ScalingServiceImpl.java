package ru.mephi.spark.banchmark.scaling;

import io.kubernetes.client.openapi.models.V1Pod;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.mephi.spark.banchmark.cache.CacheService;
import ru.mephi.spark.banchmark.kubernates.KubernetesManagingService;

import java.util.List;

@Service
@EnableScheduling
public class ScalingServiceImpl implements ScalingService {

    private final CacheService cacheService;
    private final KubernetesManagingService kubernetesManagingService;
    private final RestTemplate restTemplate;
    private final String url;

    private final String SCHEMA = "http://%s:%d/calculate";

    public ScalingServiceImpl(CacheService cacheService, KubernetesManagingService kubernetesManagingService, RestTemplateBuilder restTemplateBuilder, ScalingServiceProperties properties) {
        this.cacheService = cacheService;
        this.kubernetesManagingService = kubernetesManagingService;
        this.restTemplate = restTemplateBuilder.build();
        this.url = String.format(SCHEMA, properties.getHost(), properties.getPort());
    }

    @Override
    @Scheduled(fixedRate = 60 * 1000)
    public void scaleCluster() {
//        String uriString = UriComponentsBuilder.fromHttpUrl(url)
//                .queryParam("latency", 1000)
//                .toUriString();
//
//        Integer count = restTemplate.getForEntity(uriString, Integer.class)
//                .getBody();

        List<V1Pod> pods = kubernetesManagingService.getPodList();

        System.out.println(pods.size());
    }
}
