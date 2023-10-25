package com.nakaligoba.backend.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.zerodep.ZerodepDockerHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class DockerConfig {

    @Bean
    DockerClient dockerClient() {
        DockerHttpClient httpClient = new ZerodepDockerHttpClient.Builder()
                .dockerHost(URI.create("unix:///var/run/docker.sock"))
                .sslConfig(null)
                .build();

        return DockerClientBuilder.getInstance()
                .withDockerHttpClient(httpClient)
                .build();
    }
}
