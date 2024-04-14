package com.project.repositoryscoring.search.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.repositoryscoring.search.client.GithubClient;
import com.project.repositoryscoring.search.client.config.GithubConfigurationProperties;
import com.project.repositoryscoring.search.client.response.GithubSearchResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class GithubClientImpl implements GithubClient {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    private final GithubConfigurationProperties properties;

    @Override
    public GithubSearchResponse search(String language, String createdAt) {
        String url = this.buildUrl(language, createdAt);

        Request request = this.buildRequest(url);

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                handleUnsuccessfulResponse(response);
            }

            return objectMapper.readValue(response.body().string(), GithubSearchResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String buildUrl(String language, String createdAt) {
        StringBuilder urlBuilder = new StringBuilder(properties.getUrl())
            .append("?q=language:")
            .append(language);

        if (createdAt != null) {
            urlBuilder.append("+created:").append(createdAt);
        }

        urlBuilder.append("&sort=stars&order=desc");
        return urlBuilder.toString();
    }

    private Request buildRequest(String url) {
        return new Request.Builder()
            .get()
            .url(url)
            .addHeader("X-GitHub-Api-Version", "2022-11-28")
            .addHeader("Accept", "application/vnd.github+json")
            .addHeader("Authorization", "Bearer " + properties.getToken())
            .build();
    }

    private void handleUnsuccessfulResponse(Response response) throws IOException {
        throw new IOException("Failed to fetch repositories. HTTP error code: " + response.code());
    }
}
