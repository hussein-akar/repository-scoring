package com.project.repositoryscoring.search.client.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.repositoryscoring.search.client.config.GithubConfigurationProperties;
import java.io.IOException;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
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

@ExtendWith(MockitoExtension.class)
class GithubClientImplTest {

    public static final String LANGUAGE = "java";

    private MockWebServer mockWebServer;

    @Spy
    OkHttpClient httpClient;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    GithubConfigurationProperties properties;

    @InjectMocks
    private GithubClientImpl underTest;

    @BeforeEach
    void setUp() {
        mockWebServer = new MockWebServer();

        when(properties.getUrl()).thenReturn(mockWebServer.url("/search").toString());
        when(properties.getToken()).thenReturn("github-token");
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @SneakyThrows
    void shouldIncludeLanguageFilterWhenCallingEndpoint() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        underTest.search(LANGUAGE, null);

        RecordedRequest actual = mockWebServer.takeRequest();

        assertThat(actual.getMethod()).isEqualTo("GET");
        assertThat(actual.getPath()).startsWith("/search");
        assertThat(actual.getRequestUrl()).isNotNull();
        assertThat(actual.getRequestUrl().queryParameter("q")).isEqualTo("language:" + LANGUAGE);
    }

    @Test
    @SneakyThrows
    void shouldIncludeCreatedAtFilterWhenCreatedAtIsNotNull() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        String createdAt = "2020-01-01";
        underTest.search(LANGUAGE, createdAt);

        RecordedRequest actual = mockWebServer.takeRequest();

        assertThat(actual.getMethod()).isEqualTo("GET");
        assertThat(actual.getPath()).startsWith("/search");
        assertThat(actual.getRequestUrl()).isNotNull();
        assertThat(actual.getRequestUrl().queryParameter("q")).isEqualTo("language:java created:2020-01-01");
    }

    @Test
    @SneakyThrows
    void shouldIncludeOrderByRateDescWhenCallingThenEndpoint() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        underTest.search(LANGUAGE, null);

        RecordedRequest actual = mockWebServer.takeRequest();

        assertThat(actual.getMethod()).isEqualTo("GET");
        assertThat(actual.getPath()).startsWith("/search");
        assertThat(actual.getRequestUrl()).isNotNull();
        assertThat(actual.getRequestUrl().queryParameter("sort")).isEqualTo("stars");
        assertThat(actual.getRequestUrl().queryParameter("order")).isEqualTo("desc");
    }

    @Test
    void shouldThrowExceptionWhenCallIsNotSuccessful() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(400));

        Throwable throwable = catchThrowable(() -> underTest.search("123", null));

        assertThat(throwable).isNotNull();
    }
}