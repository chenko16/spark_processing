package ru.mephi.spark.banchmark.kubernates;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KubernetesManagingServiceImpl implements KubernetesManagingService {

    private final KubernetesClient kubernetesClient;

    @Value("${cluster.image}")
    private String image;

    @Value("${cluster.main.class}")
    private String mainClass;


    @Override
    public void deployApplication(int nodeNumber) {
        Deployment deployment = new DeploymentBuilder()
                .withApiVersion( "sparkoperator.k8s.io/v1beta2")
                .withKind("SparkApplication")
                .withNewMetadata()
                    .withName("spark-streaming")
                    .withNamespace("default")
                    .addToLabels("app", "spark")
                .endMetadata()
                .withNewSpec()
                    .withNewTemplate()
                        .withNewMetadata()
                            .addToAnnotations("type", "Java")
                            .addToAnnotations("mode", "cluster")
                            .addToAnnotations("image", image)
                            .addToAnnotations("imagePullPolicy", "Always")
                            .addToAnnotations("mainClass", mainClass)
                            .addToAnnotations("sparkVersion", "3.1.1")
                        .endMetadata()
                    .endTemplate()
                    .withNewTemplate()
                        .withNewMetadata()
                            .addToAnnotations("name", "test-volume")
                            .addToAnnotations("hostPath", "/tmp")
                        .endMetadata()
                    .endTemplate()
                    .withNewTemplate()
                        .withNewMetadata()
                            .addToAnnotations("cores", "1")
                            .addToAnnotations("coreLimit", "1200m")
                            .addToAnnotations("memory", "512m")
                            .addToAnnotations("serviceAccount", "spark")
                            .addToAnnotations("volumeMounts", "/tmp")
                        .endMetadata()
                    .endTemplate()
                    .withNewTemplate()
                        .withNewMetadata()
                            .addToAnnotations("cores", "1")
                            .addToAnnotations("instances", String.valueOf(nodeNumber))
                            .addToAnnotations("memory", "512m")
                            .addToAnnotations("volumeMounts", "/tmp")
                        .endMetadata()
                    .endTemplate()
                    .withReplicas(1)
                .endSpec()
                .build();


        kubernetesClient.apps().deployments().inNamespace("default").createOrReplace(deployment);
    }
}
