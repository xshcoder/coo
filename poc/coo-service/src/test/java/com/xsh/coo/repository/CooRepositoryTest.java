package com.xsh.coo.repository;

import com.xsh.coo.model.Coo;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(CooRepository.class)
@AutoConfigureEmbeddedDatabase
@Sql("/test-data.sql")
class CooRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CooRepository cooRepository;

    private UUID userId;
    private UUID cooId1;
    private UUID cooId2;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        cooId1 = UUID.randomUUID();
        cooId2 = UUID.randomUUID();

        // Insert test user
        jdbcTemplate.update(
            "INSERT INTO users (id, handle, name, email, bio, created_at) VALUES (?, ?, ?, ?, ?, ?)",
            userId, "test_user", "Test User", "test@example.com", "test bio", OffsetDateTime.now()
        );

        // Insert test coos
        jdbcTemplate.update(
            "INSERT INTO coos (id, user_id, content, created_at) VALUES (?, ?, ?, ?)",
            cooId1, userId, "First test coo", OffsetDateTime.now()
        );
        jdbcTemplate.update(
            "INSERT INTO coos (id, user_id, content, created_at) VALUES (?, ?, ?, ?)",
            cooId2, userId, "Second test coo", OffsetDateTime.now()
        );
    }

    @Test
    void createCoo_ShouldReturnCreatedCoo() {
        // Arrange
        Coo newCoo = new Coo();
        newCoo.setUserId(userId);
        newCoo.setContent("New test coo");

        // Act
        Coo result = cooRepository.save(newCoo);

        // Assert
        assertNotNull(result);
        assertEquals(newCoo.getUserId(), result.getUserId());
        assertEquals(newCoo.getContent(), result.getContent());
    }

    @Test
    void getCoo_WithValidId_ShouldReturnCoo() {
        // Act
        Coo result = cooRepository.findById(cooId1).get();

        // Assert
        assertNotNull(result);
        assertEquals(cooId1, result.getId());
        assertEquals("First test coo", result.getContent());
    }

    @Test
    void getUserCoos_ShouldReturnUserCoosWithPagination() {
        // Act
        Page<Coo> result = cooRepository.findByUserId(userId, PageRequest.of(0, 10));

        // Assert
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream()
            .allMatch(coo -> coo.getUserId().equals(userId)));
    }

    @Test
    void deleteCoo_ShouldRemoveCoo() {
        // Act
        cooRepository.deleteById(cooId1);

        // Assert
        assertEquals(Optional.empty(), cooRepository.findById(cooId1));
    }
}