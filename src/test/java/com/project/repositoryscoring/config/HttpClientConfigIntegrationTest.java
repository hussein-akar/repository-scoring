package com.project.repositoryscoring.config;

import static org.assertj.core.api.Assertions.assertThat;

import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = HttpClientConfig.class)
class HttpClientConfigIntegrationTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void shouldHaveHttpClientAvailableInApplicationContextWhenApplicationStarts() {
        OkHttpClient actual = applicationContext.getBean(OkHttpClient.class);

        assertThat(actual).isNotNull();
    }
}