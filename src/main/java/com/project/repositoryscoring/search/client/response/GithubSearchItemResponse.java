package com.project.repositoryscoring.search.client.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubSearchItemResponse {

    public static final double MAX_STARS = 150_000.0;
    public static final double MAX_FORKS = 50_000.0;
    public static final long MAX_DURATION_IN_DAYS = 365 * 10;

    public static final double STARS_WEIGHT = 0.5;
    public static final double FORKS_WEIGHT = 0.4;
    public static final double RECENCY_WEIGHT = 0.1;

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

    @JsonProperty(value = "popularityScore")
    public String getPopularityScore() {
        double starsScore = STARS_WEIGHT * this.getNormalizedStars();
        double forksScore = FORKS_WEIGHT * this.getNormalizedForks();
        double recencyScore = RECENCY_WEIGHT * this.getNormalizedRecency();

        return String.format("%.2f", (starsScore + forksScore + recencyScore) * 100);
    }

    private double getNormalizedStars() {
        return Math.min(this.starsCount / MAX_STARS, 1);
    }

    private double getNormalizedForks() {
        return Math.min(this.forksCount / MAX_FORKS, 1);
    }

    private double getNormalizedRecency() {
        long durationInDays = Duration.between(this.updatedAt, Instant.now()).toDays();
        durationInDays = Math.min(durationInDays, MAX_DURATION_IN_DAYS);

        return Math.max(0, Math.min(1, 1 - ((double) durationInDays / MAX_DURATION_IN_DAYS)));
    }
}
