package com.project.repositoryscoring.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.repositoryscoring.search.client.config.GithubConfigurationProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest(classes = WebClientConfig.class)
class WebClientConfigTest {

    @MockBean
    GithubConfigurationProperties properties;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void shouldHaveHttpClientAvailableInApplicationContextWhenApplicationStarts() {
        WebClient actual = applicationContext.getBean(WebClient.class);

        assertThat(actual).isNotNull();
    }
}