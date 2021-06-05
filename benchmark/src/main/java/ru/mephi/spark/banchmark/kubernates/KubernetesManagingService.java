package ru.mephi.spark.banchmark.kubernates;

import io.fabric8.kubernetes.api.model.Node;

import java.util.List;

public interface KubernetesManagingService {

    void deployApplication(int nodeNumber);

}
