package ru.mephi.spark.banchmark.scaling;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.mephi.spark.banchmark.cache.CacheService;
import ru.mephi.spark.banchmark.kubernates.KubernetesManagingService;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class ScalingServiceImpl implements ScalingService {

    private final CacheService cacheService;
    private final KubernetesManagingService kubernetesManagingService;
    private final RestTemplate restTemplate;
    @Value("${calculation.url}") private final String url;

    @Override
//    @Scheduled(fixedRate = 60 * 1000)
    public void scaleCluster() {
        Long averageLatency = cacheService.getAverageLatency();

        String uriString = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("latency", 1000)
                .toUriString();

        Integer count = restTemplate.getForEntity(uriString, Integer.class)
                .getBody();

        kubernetesManagingService.deployApplication(count);
    }
}
