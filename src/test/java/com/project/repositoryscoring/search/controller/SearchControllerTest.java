package com.project.repositoryscoring.search.controller;

import static org.mockito.Mockito.when;

import com.project.repositoryscoring.search.client.GithubClient;
import com.project.repositoryscoring.search.client.response.GithubSearchItemResponse;
import com.project.repositoryscoring.search.client.response.GithubSearchResponse;
import java.time.Instant;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(SearchController.class)
class SearchControllerTest {

    @MockBean
    GithubClient githubClient;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @SneakyThrows
    void shouldReturnBadRequestWhenLanguageIsMissing() {
        webTestClient
            .get()
            .uri(SearchController.URL_PATH.concat("/repositories?created_at=2024-01-01"))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.status").isEqualTo(400)
            .jsonPath("$.title").isEqualTo("Bad Request")
            .jsonPath("$.detail").isEqualTo("400 BAD_REQUEST \"Required query parameter 'language' is not present.\"");
    }

    @Test
    @SneakyThrows
    void shouldReturnBadRequestWhenLanguageIsBlank() {
        webTestClient
            .get()
            .uri(SearchController.URL_PATH.concat("/repositories?language=&created_at=2024-01-01"))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.status").isEqualTo(400)
            .jsonPath("$.title").isEqualTo("Bad Request")
            .jsonPath("$.detail").isEqualTo("400 BAD_REQUEST \"Validation failure\"");
    }

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"abc", "2024-01-01T00:00", "2024-01-01T00:00:00", "2024-01-01T00:00:00Z"})
    void shouldReturnBadRequestWhenCreatedAtFormatDoesNotMatchThePattern(String createdAt) {
        webTestClient
            .get()
            .uri(SearchController.URL_PATH.concat("/repositories?language=java&created_at=".concat(createdAt)))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.status").isEqualTo(400)
            .jsonPath("$.title").isEqualTo("Bad Request")
            .jsonPath("$.detail").isEqualTo("400 BAD_REQUEST \"Validation failure\"");
    }

    @Test
    @SneakyThrows
    void shouldReturnOkWhenParametersAreValid() {
        GithubSearchResponse githubSearchResponse = new GithubSearchResponse(List.of(
            new GithubSearchItemResponse(
                1,
                "repository-name-1",
                10,
                5,
                Instant.parse("2020-01-01T00:00:00Z"),
                Instant.parse("2020-01-01T00:00:00Z")
            )
        ));

        when(githubClient.search("java", "2024-01-01")).thenReturn(Mono.just(githubSearchResponse));

        webTestClient
            .get()
            .uri(SearchController.URL_PATH.concat("/repositories?language=java&created_at=2024-01-01"))
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.items.size()").isEqualTo(1)
            .jsonPath("$.items[0].id").isEqualTo(1)
            .jsonPath("$.items[0].name").isEqualTo("repository-name-1")
            .jsonPath("$.items[0].starsCount").isEqualTo(10)
            .jsonPath("$.items[0].forksCount").isEqualTo(5)
            .jsonPath("$.items[0].createdAt").isEqualTo("2020-01-01T00:00:00Z")
            .jsonPath("$.items[0].updatedAt").isEqualTo("2020-01-01T00:00:00Z");
    }
}