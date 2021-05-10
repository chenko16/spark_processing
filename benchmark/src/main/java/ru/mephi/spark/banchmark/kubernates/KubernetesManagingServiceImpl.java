package ru.mephi.spark.banchmark.kubernates;

import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class KubernetesManagingServiceImpl implements KubernetesManagingService {

    private final KubernetesClient kubernetesClient;

    @SneakyThrows
    @Override
    public List<Node> getPodList() {
        return kubernetesClient.nodes().list().getItems();
    }
}
