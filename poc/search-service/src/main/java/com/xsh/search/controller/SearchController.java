package com.xsh.search.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import com.xsh.search.service.SearchService;
import com.xsh.search.model.User;
import com.xsh.search.model.Coo;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/users")
    public ResponseEntity<Page<User>> searchUsers(
            @RequestParam(required = false) String text,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(searchService.searchUsers(text, page, size));
    }

    @GetMapping("/coos")
    public ResponseEntity<Page<Coo>> searchCoos(
            @RequestParam String text,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(searchService.searchCoos(text, page, size));
    }
}