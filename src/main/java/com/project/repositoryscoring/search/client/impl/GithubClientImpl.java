package com.project.repositoryscoring.search.client.impl;

import com.project.repositoryscoring.exception.GithubApiClientException;
import com.project.repositoryscoring.exception.GithubApiServerException;
import com.project.repositoryscoring.search.client.GithubClient;
import com.project.repositoryscoring.search.client.response.GithubSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
class GithubClientImpl implements GithubClient {

    @Qualifier("githubWebclient")
    private final WebClient webClient;

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
            .onStatus(HttpStatusCode::is4xxClientError, this::handleClientError)
            .onStatus(HttpStatusCode::is5xxServerError, this::handleServerError)
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


    private Mono<? extends Throwable> handleClientError(ClientResponse clientResponse) {
        return clientResponse.createException().flatMap(
            err -> Mono.error(new GithubApiClientException(err.getMessage())
            ));
    }

    private Mono<? extends Throwable> handleServerError(ClientResponse serverResponse) {
        return serverResponse.createException().flatMap(
            err -> Mono.error(new GithubApiServerException(err.getMessage())
            ));
    }
}
