package com.project.repositoryscoring;

import com.project.repositoryscoring.search.client.config.GithubConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(GithubConfigurationProperties.class)
public class RepositoryScoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(RepositoryScoringApplication.class, args);
    }

}
