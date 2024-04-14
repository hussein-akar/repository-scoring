package com.project.repositoryscoring.search.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SearchController.URL_PATH)
public class SearchController {

    public static final String URL_PATH = "/api/v1/search";

    @GetMapping("/repositories")
    public ResponseEntity<Void> search(@RequestParam String language, @RequestParam(value = "created_at", required = false) String createdAt) {
        return ResponseEntity.ok().build();
    }
}