package com.project.repositoryscoring.search.client;

import com.project.repositoryscoring.search.client.response.GithubSearchResponse;
import reactor.core.publisher.Mono;

public interface GithubClient {

    Mono<GithubSearchResponse> search(String language, String createdAt);
}
