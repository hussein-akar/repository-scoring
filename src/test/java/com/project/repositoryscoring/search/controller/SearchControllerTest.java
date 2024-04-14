package com.project.repositoryscoring.search.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

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
        mockMvc.perform(MockMvcRequestBuilders
                .get(SearchController.URL_PATH.concat("/repositories?language=java&created_at=2024-01-01")))
            .andExpect(status().isOk());
    }
}