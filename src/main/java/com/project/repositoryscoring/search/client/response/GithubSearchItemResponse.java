package com.project.repositoryscoring.search.client.response;

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

    @JsonProperty("stargazers_count")
    private Integer starsCount;

    @JsonProperty("forks_count")
    private Integer forksCount;

    @JsonProperty("created_at")
    protected Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;
}
