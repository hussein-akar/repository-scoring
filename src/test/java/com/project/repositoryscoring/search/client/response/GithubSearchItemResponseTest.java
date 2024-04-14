package com.project.repositoryscoring.search.client.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GithubSearchItemResponseTest {

    @Nested
    class getPopularityScore {

        @Test
        void shouldSetPopularityScoreToZeroWhenRepositoryIsNotPopular() {
            Instant tenYearsAgo = LocalDateTime.now().minusYears(10).minusMonths(1).toInstant(ZoneOffset.UTC);

            GithubSearchItemResponse githubSearchItemResponse = new GithubSearchItemResponse(1, "Repository 1", 0, 0, Instant.now(), tenYearsAgo);

            String actual = githubSearchItemResponse.getPopularityScore();

            assertThat(actual).isEqualTo("0,00");
        }

        @Test
        void shouldSetPopularityScoreToHundredWhenRepositoryIsSoPopular() {
            GithubSearchItemResponse githubSearchItemResponse = new GithubSearchItemResponse(1, "Repository 1", 150_000, 50_000, Instant.now(),
                Instant.now());

            String actual = githubSearchItemResponse.getPopularityScore();

            assertThat(actual).isEqualTo("100,00");
        }

        @Test
        void shouldNormalizeValuesToGetMaxOfHundredWhenValuesExceedsMaxValues() {
            GithubSearchItemResponse githubSearchItemResponse = new GithubSearchItemResponse(1, "Repository 1", 1_000_000, 1_000_000, Instant.now(),
                Instant.now());

            String actual = githubSearchItemResponse.getPopularityScore();

            assertThat(actual).isEqualTo("100,00");
        }
    }
}