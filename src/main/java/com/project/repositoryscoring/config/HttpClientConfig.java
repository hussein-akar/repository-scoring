package com.project.repositoryscoring.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {

    @Bean
    public OkHttpClient httpClient() {
        return new OkHttpClient();
    }
}