package com.project.repositoryscoring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Repository Scoring")
                .description(
                    "This project allows users to search GitHub repositories, which employs a scoring mechanism based on stars, forks, and recency of updates.")
                .version("1.0.0")
                .contact(new Contact().name("Hussein Akar").email("hussein.akar992@gmail.com"))
                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
