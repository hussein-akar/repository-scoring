package com.project.repositoryscoring.search.client.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.project.repositoryscoring.search.client.config.GithubConfigurationProperties;
import com.project.repositoryscoring.search.client.response.GithubSearchItemResponse;
import com.project.repositoryscoring.search.client.response.GithubSearchResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@ExtendWith(MockitoExtension.class)
class GithubClientImplTest {

    public static final String LANGUAGE = "java";

    private MockWebServer mockWebServer;

    @Spy
    WebClient webClient;

    @Mock
    GithubConfigurationProperties properties;

    @InjectMocks
    private GithubClientImpl underTest;

    @BeforeEach
    void setUp() {
        mockWebServer = new MockWebServer();

        when(properties.getUrl()).thenReturn(mockWebServer.url("/search").toString());
        when(properties.getToken()).thenReturn("github-token");

        underTest = new GithubClientImpl(properties);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void shouldThrowExceptionWhenCallIsNotSuccessful() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(400));

        underTest.search(LANGUAGE, null).subscribe(
            response -> assertThat(response).isNull(),
            error -> assertThat(error).isNotNull()
        );
    }

    @Test
    @SneakyThrows
    void shouldIncludeLanguageFilterWhenCallingEndpoint() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("""
                {
                    "total_count": 0,
                    "incomplete_results": false,
                    "items": []
                }
                """
            ));

        underTest.search(LANGUAGE, null).subscribe();

        RecordedRequest actual = mockWebServer.takeRequest();

        assertThat(actual.getMethod()).isEqualTo("GET");
        assertThat(actual.getPath()).startsWith("/search");
        assertThat(actual.getRequestUrl()).isNotNull();
        assertThat(actual.getRequestUrl().queryParameter("q")).isEqualTo("language:" + LANGUAGE);
    }

    @Test
    @SneakyThrows
    void shouldIncludeCreatedAtFilterWhenCreatedAtIsNotNull() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("""
                {
                    "total_count": 0,
                    "incomplete_results": false,
                    "items": []
                }
                """
            ));

        String createdAt = "2020-01-01";
        underTest.search(LANGUAGE, createdAt).subscribe();

        RecordedRequest actual = mockWebServer.takeRequest();

        assertThat(actual.getMethod()).isEqualTo("GET");
        assertThat(actual.getPath()).startsWith("/search");
        assertThat(actual.getRequestUrl()).isNotNull();
        assertThat(actual.getRequestUrl().queryParameter("q")).isEqualTo("language:java created:2020-01-01");
    }

    @Test
    @SneakyThrows
    void shouldIncludeOrderByRateDescWhenCallingThenEndpoint() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("""
                {
                    "total_count": 0,
                    "incomplete_results": false,
                    "items": []
                }
                """
            ));

        underTest.search(LANGUAGE, null).subscribe();

        RecordedRequest actual = mockWebServer.takeRequest();

        assertThat(actual.getMethod()).isEqualTo("GET");
        assertThat(actual.getPath()).startsWith("/search");
        assertThat(actual.getRequestUrl()).isNotNull();
        assertThat(actual.getRequestUrl().queryParameter("sort")).isEqualTo("stars");
        assertThat(actual.getRequestUrl().queryParameter("order")).isEqualTo("desc");
    }

    @Test
    @SneakyThrows
    void validateMappingToCorrectPropertiesWhenReceivingTheResponse() {
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                    {
                        "total_count": 0,
                        "incomplete_results": false,
                        "items": [
                            {
                                "id": 1,
                                "name": "repository-name-1",
                                "stargazers_count": 10,
                                "forks_count": 5,
                                "created_at": "2020-01-01T00:00:00Z",
                                "updated_at": "2020-01-01T00:00:00Z",
                                "owner": {
                                    "login": "owner-login"
                                },
                                "html_url": "http://repository-url/index.html"
                            },
                            {
                                "id": 2,
                                "name": "repository-name-2",
                                "stargazers_count": 0,
                                "forks_count": 3,
                                "created_at": "2023-09-01T00:00:00Z",
                                "updated_at": "2020-10-01T00:00:00Z",
                                "html_url": "http://repository-url/index.html"
                            }
                        ]
                    }
                    """)
        );

        underTest.search(LANGUAGE, null)
            .subscribe(result -> assertThat(result).usingRecursiveComparison().isEqualTo(
                new GithubSearchResponse(
                    List.of(
                        new GithubSearchItemResponse(
                            1,
                            "repository-name-1",
                            10,
                            5,
                            Instant.parse("2020-01-01T00:00:00Z"),
                            Instant.parse("2020-01-01T00:00:00Z")
                        ),
                        new GithubSearchItemResponse(
                            2,
                            "repository-name-2",
                            0,
                            3,
                            Instant.parse("2023-09-01T00:00:00Z"),
                            Instant.parse("2020-10-01T00:00:00Z")
                        )
                    )
                )
            ));
    }
}