package com.project.repositoryscoring.search.client.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "application.third-party.github.url=https://api.github.com",
    "application.third-party.github.token=github-token"
})
@Import(GithubConfigurationPropertiesTest.TestConfig.class)
class GithubConfigurationPropertiesTest {

    @Autowired
    private GithubConfigurationProperties githubProperties;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public GithubConfigurationProperties githubConfigurationProperties() {
            return new GithubConfigurationProperties();
        }
    }

    @Test
    public void testGithubConfigurationProperties() {
        assertThat(githubProperties).isNotNull();
        assertThat(githubProperties.getUrl()).isEqualTo("https://api.github.com");
        assertThat(githubProperties.getToken()).isEqualTo("github-token");
    }
}