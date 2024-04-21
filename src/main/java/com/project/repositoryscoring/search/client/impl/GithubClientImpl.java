package com.project.repositoryscoring.search.client.impl;

import com.project.repositoryscoring.search.client.GithubClient;
import com.project.repositoryscoring.search.client.config.GithubConfigurationProperties;
import com.project.repositoryscoring.search.client.response.GithubSearchResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
class GithubClientImpl implements GithubClient {

    private final WebClient webClient;

    public GithubClientImpl(GithubConfigurationProperties properties) {
        this.webClient = WebClient.builder()
            .baseUrl(properties.getUrl())
            .defaultHeader("Authorization", "Bearer " + properties.getToken())
            .defaultHeader("Accept", "application/vnd.github+json")
            .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
            .build();
    }

    @Override
    public Mono<GithubSearchResponse> search(String language, String createdAt) {
        String query = this.buildQuery(language, createdAt);

        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .queryParam("q", query)
                .queryParam("sort", "stars")
                .queryParam("order", "desc")
                .build())
            .retrieve()
            .bodyToMono(GithubSearchResponse.class);
    }

    private String buildQuery(String language, String createdAt) {
        StringBuilder urlBuilder = new StringBuilder()
            .append("language:").append(language);

        if (createdAt != null) {
            urlBuilder.append("+created:").append(createdAt);
        }

        return urlBuilder.toString();
    }
}
