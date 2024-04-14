package com.project.repositoryscoring.search.client.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubSearchItemResponse {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonAlias({"stargazers_count"})
    @JsonProperty(value = "starsCount")
    private Integer starsCount;

    @JsonAlias({"forks_count"})
    @JsonProperty(value = "forksCount")
    private Integer forksCount;

    @JsonAlias({"created_at"})
    @JsonProperty(value = "createdAt")
    protected Instant createdAt;

    @JsonAlias("updated_at")
    @JsonProperty(value = "updatedAt")
    private Instant updatedAt;
}
