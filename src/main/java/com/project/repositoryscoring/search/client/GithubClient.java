package com.project.repositoryscoring.search.client;

import com.project.repositoryscoring.search.client.response.GithubSearchResponse;

public interface GithubClient {

    GithubSearchResponse search(String language, String createdAt);
}
