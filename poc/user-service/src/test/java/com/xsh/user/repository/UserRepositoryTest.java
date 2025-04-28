package com.xsh.user.repository;

import com.xsh.user.model.User;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureEmbeddedDatabase
@Import(UserRepository.class)
@Sql("/test-data.sql")
class UserRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    private UUID TEST_USER_ID;
    private String TEST_USER_HANDLE;

    @BeforeEach
    void setUp() {
        TEST_USER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        TEST_USER_HANDLE = "testuser1";

        // Insert test users
        jdbcTemplate.update(
            "INSERT INTO users (id, handle, name, email, bio, logo) VALUES (?, ?, ?, ?, ?, ?)",
            TEST_USER_ID, TEST_USER_HANDLE, "Test User 1", "test1@example.com", "Bio for test user 1", "logo1.png"
        );
        jdbcTemplate.update(
            "INSERT INTO users (id, handle, name, email, bio, logo) VALUES (?, ?, ?, ?, ?, ?)",
            UUID.fromString("223e4567-e89b-12d3-a456-426614174000"), "testuser2", "Test User 2", "test2@example.com", "Bio for test user 2", "logo2.png"
        );
        jdbcTemplate.update(
            "INSERT INTO users (id, handle, name, email, bio, logo) VALUES (?, ?, ?, ?, ?, ?)",
            UUID.fromString("323e4567-e89b-12d3-a456-426614174000"), "testuser3", "Test User 3", "test3@example.com", "Bio for test user 3", "logo3.png"
        );
    }

    @Test
    void findUsers_ShouldReturnPageOfUsers() {
        // Act
        Page<User> userPage = userRepository.findUsers(PageRequest.of(0, 2));

        // Assert
        assertEquals(3, userPage.getTotalElements());
        assertEquals(2, userPage.getContent().size());
        assertEquals(TEST_USER_HANDLE, userPage.getContent().get(0).getHandle());
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        // Act
        Optional<User> user = userRepository.findById(TEST_USER_ID);

        // Assert
        assertTrue(user.isPresent());
        assertEquals(TEST_USER_HANDLE, user.get().getHandle());
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<User> user = userRepository.findById(UUID.randomUUID());

        // Assert
        assertTrue(user.isEmpty());
    }

    @Test
    void findByHandle_WhenUserExists_ShouldReturnUser() {
        // Act
        Optional<User> user = userRepository.findByHandle(TEST_USER_HANDLE);

        // Assert
        assertTrue(user.isPresent());
        assertEquals(TEST_USER_ID, user.get().getId());
    }

    @Test
    void findByHandle_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<User> user = userRepository.findByHandle("nonexistent");

        // Assert
        assertTrue(user.isEmpty());
    }

    @Test
    void save_ShouldCreateNewUser() {
        // Arrange
        User newUser = new User();
        newUser.setHandle("newuser");
        newUser.setName("New User");
        newUser.setEmail("new@example.com");
        newUser.setBio("New user bio");

        // Act
        User savedUser = userRepository.save(newUser);

        // Assert
        assertNotNull(savedUser.getId());
        assertEquals("newuser", savedUser.getHandle());
        assertEquals("New User", savedUser.getName());
    }

    @Test
    void save_ShouldUpdateExistingUser() {
        // Arrange
        User existingUser = userRepository.findById(TEST_USER_ID).orElseThrow();
        existingUser.setName("Updated Name");
        existingUser.setBio("Updated bio");

        // Act
        User updatedUser = userRepository.save(existingUser);

        // Assert
        assertEquals(TEST_USER_ID, updatedUser.getId());
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("Updated bio", updatedUser.getBio());
    }

    @Test
    void deleteById_ShouldRemoveUser() {
        // Act
        userRepository.deleteById(TEST_USER_ID);

        // Assert
        Optional<User> deletedUser = userRepository.findById(TEST_USER_ID);
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    void findByIds_ShouldReturnMatchingUsers() {
        // Arrange
        List<UUID> ids = Arrays.asList(
            TEST_USER_ID,
            UUID.fromString("223e4567-e89b-12d3-a456-426614174000")
        );
        
        // Act
        List<User> users = userRepository.findByIds(ids);
        
        // Assert
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getId().equals(TEST_USER_ID)));
        assertTrue(users.stream().anyMatch(u -> u.getId().equals(UUID.fromString("223e4567-e89b-12d3-a456-426614174000"))));
    }

    @Test
    void findByIds_WithEmptyList_ShouldReturnEmptyList() {
        // Act
        List<User> users = userRepository.findByIds(Collections.emptyList());
        
        // Assert
        assertTrue(users.isEmpty());
    }
}