package com.xsh.search.service;

import com.xsh.search.model.User;
import com.xsh.search.model.Coo;
import com.xsh.search.repository.SearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class SearchServiceTest {

    @Mock
    private SearchRepository searchRepository;

    private SearchService searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        searchService = new SearchService(searchRepository);
    }

    @Test
    void searchUsers_ShouldReturnMatchingUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setHandle("john_doe");
        user1.setName("John Doe");
        user1.setEmail("john@example.com");
        user1.setCreatedAt(OffsetDateTime.now());

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setHandle("jane_doe");
        user2.setName("Jane Doe");
        user2.setEmail("jane@example.com");
        user2.setCreatedAt(OffsetDateTime.now());

        Page<User> expectedPage = new PageImpl<>(Arrays.asList(user1, user2));
        when(searchRepository.searchUsers(eq("doe"), any(PageRequest.class))).thenReturn(expectedPage);

        // Act
        Page<User> result = searchService.searchUsers("doe", 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("john_doe", result.getContent().get(0).getHandle());
        assertEquals("jane_doe", result.getContent().get(1).getHandle());
    }

    @Test
    void searchCoos_ShouldReturnMatchingCoos() {
        // Arrange
        Coo coo1 = new Coo();
        coo1.setId(UUID.randomUUID());
        coo1.setUserId(UUID.randomUUID());
        coo1.setContent("Hello World!");
        coo1.setCreatedAt(OffsetDateTime.now());

        Coo coo2 = new Coo();
        coo2.setId(UUID.randomUUID());
        coo2.setUserId(UUID.randomUUID());
        coo2.setContent("Hello Java World!");
        coo2.setCreatedAt(OffsetDateTime.now());

        Page<Coo> expectedPage = new PageImpl<>(Arrays.asList(coo1, coo2));
        when(searchRepository.searchCoos(eq("Hello"), any(PageRequest.class))).thenReturn(expectedPage);

        // Act
        Page<Coo> result = searchService.searchCoos("Hello", 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().get(0).getContent().contains("Hello"));
        assertTrue(result.getContent().get(1).getContent().contains("Hello"));
    }

    @Test
    void searchCoos_WithEmptyQuery_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            searchService.searchCoos("", 0, 10);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            searchService.searchCoos(null, 0, 10);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            searchService.searchCoos("  ", 0, 10);
        });
    }
}