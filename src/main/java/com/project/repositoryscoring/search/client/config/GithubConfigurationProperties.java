package com.project.repositoryscoring.search.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.third-party.github")
public class GithubConfigurationProperties {

    private String url;
    private String token;
}