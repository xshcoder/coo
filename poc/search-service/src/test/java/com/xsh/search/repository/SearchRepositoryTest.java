package com.xsh.search.repository;

import com.xsh.search.model.User;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;

import com.xsh.search.model.Coo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(SearchRepository.class)
@AutoConfigureEmbeddedDatabase
@Sql("/test-data.sql")
class SearchRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SearchRepository searchRepository;

    private UUID userId1;
    private UUID userId2;
    private UUID cooId1;
    private UUID cooId2;

    @BeforeEach
    void setUp() {
        // Insert test users
        userId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();
        
        jdbcTemplate.update(
            "INSERT INTO users (id, handle, name, email, created_at) VALUES (?, ?, ?, ?, ?)",
            userId1, "john_doe", "John Doe", "john@example.com", OffsetDateTime.now()
        );
        jdbcTemplate.update(
            "INSERT INTO users (id, handle, name, email, created_at) VALUES (?, ?, ?, ?, ?)",
            userId2, "jane_doe", "Jane Doe", "jane@example.com", OffsetDateTime.now()
        );

        // Insert test coos
        cooId1 = UUID.randomUUID();
        cooId2 = UUID.randomUUID();
        
        jdbcTemplate.update(
            "INSERT INTO coos (id, user_id, content, created_at) VALUES (?, ?, ?, ?)",
            cooId1, userId1, "Hello World!", OffsetDateTime.now()
        );
        jdbcTemplate.update(
            "INSERT INTO coos (id, user_id, content, created_at) VALUES (?, ?, ?, ?)",
            cooId2, userId2, "Hello Java World!", OffsetDateTime.now()
        );
    }

    @Test
    void searchUsers_ByHandle_ShouldReturnMatchingUsers() {
        // Act
        Page<User> result = searchRepository.searchUsers("john", PageRequest.of(0, 10));

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals("john_doe", result.getContent().get(0).getHandle());
    }

    @Test
    void searchUsers_ByName_ShouldReturnMatchingUsers() {
        // Act
        Page<User> result = searchRepository.searchUsers("Doe", PageRequest.of(0, 10));

        // Assert
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream()
            .allMatch(user -> user.getName().contains("Doe")));
    }

    @Test
    void searchUsers_WithPagination_ShouldReturnCorrectPage() {
        // Act
        Page<User> result = searchRepository.searchUsers("doe", PageRequest.of(0, 1));

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void searchCoos_ShouldReturnMatchingCoos() {
        // Act
        Page<Coo> result = searchRepository.searchCoos("Hello", PageRequest.of(0, 10));

        // Assert
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream()
            .allMatch(coo -> coo.getContent().contains("Hello")));
    }

    @Test
    void searchCoos_WithSpecificWord_ShouldReturnMatchingCoos() {
        // Act
        Page<Coo> result = searchRepository.searchCoos("Java", PageRequest.of(0, 10));

        // Assert
        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().get(0).getContent().contains("Java"));
    }

    @Test
    void searchCoos_WithPagination_ShouldReturnCorrectPage() {
        // Act
        Page<Coo> result = searchRepository.searchCoos("Hello", PageRequest.of(0, 1));

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals(2, result.getTotalElements());
    }
}