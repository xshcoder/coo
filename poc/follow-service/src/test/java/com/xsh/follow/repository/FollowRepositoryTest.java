package com.xsh.follow.repository;

import com.xsh.follow.model.Follow;
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

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(FollowRepository.class)
@AutoConfigureEmbeddedDatabase
@Sql("/test-data.sql")
class FollowRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FollowRepository followRepository;

    private UUID userId1;
    private UUID userId2;
    private UUID userId3;

    @BeforeEach
    void setUp() {
        userId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();
        userId3 = UUID.randomUUID();

        // Insert test users
        jdbcTemplate.update(
            "INSERT INTO users (id, handle, name, email, bio, created_at) VALUES (?, ?, ?, ?, ?, ?)",
            userId1, "user1", "User One", "user1@example.com","user1 bio", OffsetDateTime.now()
        );
        jdbcTemplate.update(
            "INSERT INTO users (id, handle, name, email, bio, created_at) VALUES (?, ?, ?, ?, ?, ?)",
            userId2, "user2", "User Two", "user2@example.com", "user2 bio", OffsetDateTime.now()
        );
        jdbcTemplate.update(
            "INSERT INTO users (id, handle, name, email, bio, created_at) VALUES (?, ?, ?, ?, ?, ?)",
            userId3, "user3", "User Three", "user3@example.com", "user3 bio", OffsetDateTime.now()
        );

        // Create some follow relationships
        jdbcTemplate.update(
            "INSERT INTO follows (id, follower_id, followed_id, followed_at) VALUES (?, ?, ?, ?)",
            UUID.randomUUID(), userId1, userId2, OffsetDateTime.now()
        );
        jdbcTemplate.update(
            "INSERT INTO follows (id, follower_id, followed_id, followed_at) VALUES (?, ?, ?, ?)",
            UUID.randomUUID(), userId3, userId2, OffsetDateTime.now()
        );
    }

    @Test
    void save_ShouldCreateNewFollow() {
        // Arrange
        Follow follow = new Follow(userId2, userId3);

        // Act
        Follow result = followRepository.save(follow);

        // Assert
        assertNotNull(result);
        assertEquals(userId2, result.getFollowerId());
        assertEquals(userId3, result.getFollowedId());
        assertNotNull(result.getFollowedAt());
    }

    @Test
    void exists_ShouldReturnTrue_WhenFollowExists() {
        // Act
        boolean exists = followRepository.exists(userId1, userId2);

        // Assert
        assertTrue(exists);
    }

    @Test
    void exists_ShouldReturnFalse_WhenFollowDoesNotExist() {
        // Act
        boolean exists = followRepository.exists(userId1, userId3);

        // Assert
        assertFalse(exists);
    }

    @Test
    void delete_ShouldRemoveFollow() {
        // Act
        followRepository.delete(userId1, userId2);

        // Assert
        assertFalse(followRepository.exists(userId1, userId2));
    }

    @Test
    void findFollowersByUserId_ShouldReturnFollowersWithPagination() {
        // Act
        Page<Follow> result = followRepository.findFollowersByUserId(userId2, PageRequest.of(0, 10));

        // Assert
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream()
            .allMatch(follow -> follow.getFollowedId().equals(userId2)));
    }

    @Test
    void findFollowingByUserId_ShouldReturnFollowingWithPagination() {
        // Act
        Page<Follow> result = followRepository.findFollowingByUserId(userId1, PageRequest.of(0, 10));

        // Assert
        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().stream()
            .allMatch(follow -> follow.getFollowerId().equals(userId1)));
    }

    @Test
    void getFollowersCount_ShouldReturnCorrectCount() {
        // Act
        long count = followRepository.getFollowersCount(userId2);

        // Assert
        assertEquals(2, count);
    }

    @Test
    void getFollowingCount_ShouldReturnCorrectCount() {
        // Act
        long count = followRepository.getFollowingCount(userId1);

        // Assert
        assertEquals(1, count);
    }
}