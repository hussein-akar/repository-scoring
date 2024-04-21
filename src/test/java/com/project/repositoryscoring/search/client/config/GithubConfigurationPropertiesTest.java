package com.project.repositoryscoring.search.client.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "application.third-party.github.url=https://api.github.com",
    "application.third-party.github.token=github-token"
})
class GithubConfigurationPropertiesTest {

    @Autowired
    private GithubConfigurationProperties properties;

    @Test
    public void testGithubConfigurationProperties() {
        assertThat(properties).isNotNull();
        assertThat(properties.getUrl()).isEqualTo("https://api.github.com");
        assertThat(properties.getToken()).isEqualTo("github-token");
    }
}