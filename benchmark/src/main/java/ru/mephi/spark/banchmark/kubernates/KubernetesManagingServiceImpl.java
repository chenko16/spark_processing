package ru.mephi.spark.banchmark.kubernates;

import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class KubernetesManagingServiceImpl implements KubernetesManagingService {

    @SneakyThrows
    @Override
    public List<Node> getPodList() {
        KubernetesClient client = new DefaultKubernetesClient("http://192.168.12.87:6443");

        return client.nodes().list().getItems();
    }
}
