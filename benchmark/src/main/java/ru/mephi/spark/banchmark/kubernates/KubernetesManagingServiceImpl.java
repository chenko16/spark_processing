package ru.mephi.spark.banchmark.kubernates;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.List;

@Service
public class KubernetesManagingServiceImpl implements KubernetesManagingService {

    @SneakyThrows
    @Override
    public List<V1Pod> getPodList() {
        File file = new File(getClass().getResource("kube.yaml").getFile());
        FileReader configReader = new FileReader(file);

        ApiClient client = ClientBuilder
                .kubeconfig(KubeConfig.loadKubeConfig(configReader))
                .build();

        CoreV1Api api = new CoreV1Api(client);
        V1PodList podList = api.listNamespacedPod("default", null, false, null, null,
                null, null, null, null, null, null);
        podList.getItems().stream()
                .forEach(item -> System.out.println(item.toString()));

        return podList.getItems();
    }
}
