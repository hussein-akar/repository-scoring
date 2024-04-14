package com.project.repositoryscoring.search.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.repositoryscoring.search.client.GithubClient;
import com.project.repositoryscoring.search.client.response.GithubSearchItemResponse;
import com.project.repositoryscoring.search.client.response.GithubSearchResponse;
import java.time.Instant;
import java.util.List;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @MockBean
    GithubClient githubClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    void shouldReturnBadRequestWhenLanguageIsMissing() {
        mockMvc.perform(MockMvcRequestBuilders
                .get(SearchController.URL_PATH.concat("/repositories?created_at=2024-01-01")))
            .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void shouldReturnBadRequestWhenLanguageIsBlank() {
        mockMvc.perform(MockMvcRequestBuilders
                .get(SearchController.URL_PATH.concat("/repositories?language=&created_at=2024-01-01")))
            .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"abc", "2024-01-01T00:00", "2024-01-01T00:00:00", "2024-01-01T00:00:00Z"})
    void shouldReturnBadRequestWhenCreatedAtFormatDoesNotMatchThePattern(String createdAt) {
        mockMvc.perform(MockMvcRequestBuilders
                .get(SearchController.URL_PATH.concat("/repositories?language=java&created_at=".concat(createdAt))))
            .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void shouldReturnOkWhenParametersAreValid() {
        GithubSearchResponse githubSearchResponse = new GithubSearchResponse(List.of(
            new GithubSearchItemResponse(
                1,
                "repository-name-1",
                10,
                5,
                Instant.parse("2020-01-01T00:00:00Z"),
                Instant.parse("2020-01-01T00:00:00Z")
            )
        ));

        when(githubClient.search("java", "2024-01-01")).thenReturn(githubSearchResponse);

        mockMvc.perform(MockMvcRequestBuilders
                .get(SearchController.URL_PATH.concat("/repositories?language=java&created_at=2024-01-01")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.items.size()", Matchers.is(1)))
            .andExpect(jsonPath("$.items[0].id", Matchers.is(1)))
            .andExpect(jsonPath("$.items[0].name", Matchers.is("repository-name-1")))
            .andExpect(jsonPath("$.items[0].starsCount", Matchers.is(10)))
            .andExpect(jsonPath("$.items[0].forksCount", Matchers.is(5)))
            .andExpect(jsonPath("$.items[0].createdAt", Matchers.is("2020-01-01T00:00:00Z")))
            .andExpect(jsonPath("$.items[0].updatedAt", Matchers.is("2020-01-01T00:00:00Z")));
    }
}