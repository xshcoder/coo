package com.xsh.search.service;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.xsh.search.model.User;
import com.xsh.search.model.Coo;
import com.xsh.search.repository.SearchRepository;

@Service
public class SearchService {

    private final SearchRepository searchRepository;

    public SearchService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public Page<User> searchUsers(String text, int page, int size) {
        return searchRepository.searchUsers(text, PageRequest.of(page, size));
    }

    public Page<Coo> searchCoos(String text, int page, int size) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Search text cannot be empty");
        }
        return searchRepository.searchCoos(text.trim(), PageRequest.of(page, size));
    }

}