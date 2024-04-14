package com.project.repositoryscoring.search.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.third-party.github")
public class GithubConfigurationProperties {

    private String url;
    private String token;
}