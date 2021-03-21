package ru.mephi.spark.banchmark.kubernates;

import io.kubernetes.client.openapi.models.V1Pod;

import java.util.List;

public interface KubernetesManagingService {

    List<V1Pod> getPodList();

}
