package com.project.repositoryscoring.config;

import com.project.repositoryscoring.search.client.config.GithubConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final GithubConfigurationProperties properties;

    @Bean("githubWebclient")
    public WebClient githubWebclient() {
        return WebClient.builder()
            .baseUrl(properties.getUrl())
            .defaultHeader("Authorization", "Bearer " + properties.getToken())
            .defaultHeader("Accept", "application/vnd.github+json")
            .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
            .build();
    }
}