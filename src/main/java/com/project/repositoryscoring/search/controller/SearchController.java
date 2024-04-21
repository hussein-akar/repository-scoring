package com.project.repositoryscoring.search.controller;

import com.project.repositoryscoring.search.client.GithubClient;
import com.project.repositoryscoring.search.client.response.GithubSearchResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Valid
@RestController
@RequiredArgsConstructor
@RequestMapping(SearchController.URL_PATH)
public class SearchController {

    public static final String URL_PATH = "/api/v1/search";

    private static final String MESSAGE = "created_at: Invalid date format. The format should be yyyy-mm-dd";

    private final GithubClient githubClient;

    @GetMapping("/repositories")
    public Mono<GithubSearchResponse> search(@RequestParam @NotBlank String language,
        @RequestParam(value = "created_at", required = false)
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = MESSAGE) String createdAt) {

        return githubClient.search(language, createdAt);
    }
}