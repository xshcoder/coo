package com.xsh.statistics.repository;

import com.xsh.statistics.model.Statistics;
import com.xsh.statistics.model.StatisticsSubjectType;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(StatisticsRepository.class)
@AutoConfigureEmbeddedDatabase
@Sql("/test-data.sql")
class StatisticsRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StatisticsRepository statisticsRepository;

    private UUID cooId;
    private UUID statisticsId;

    @BeforeEach
    void setUp() {
        cooId = UUID.randomUUID();
        statisticsId = UUID.randomUUID();

        // Insert test user
        UUID userId = UUID.randomUUID();
        jdbcTemplate.update(
            "INSERT INTO users (id, handle, name, email, bio, created_at) VALUES (?, ?, ?, ?, ?, ?)",
            userId, "test_user", "Test User", "test@example.com", "test bio", OffsetDateTime.now()
        );

        // Insert test coo
        jdbcTemplate.update(
            "INSERT INTO coos (id, user_id, content, created_at) VALUES (?, ?, ?, ?)",
            cooId, userId, "Test coo content", OffsetDateTime.now()
        );

        // Insert test statistics
        jdbcTemplate.update(
            "INSERT INTO statistics (id, subject_id, subject_type, replies_count, likes_count, views_count, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
            statisticsId, cooId, StatisticsSubjectType.COO.name(), 10, 20, 30, OffsetDateTime.now()
        );
    }

    @Test
    void findBySubjectIdAndSubjectType_WhenStatisticsExists_ShouldReturnStatistics() {
        // Act
        Optional<Statistics> result = statisticsRepository.findBySubjectIdAndSubjectType(cooId, StatisticsSubjectType.COO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(cooId, result.get().getSubjectId());
        assertEquals(StatisticsSubjectType.COO, result.get().getSubjectType());
        assertEquals(10, result.get().getRepliesCount());
        assertEquals(20, result.get().getLikesCount());
        assertEquals(30, result.get().getViewsCount());
    }

    @Test
    void findBySubjectIdAndSubjectType_WhenStatisticsDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<Statistics> result = statisticsRepository.findBySubjectIdAndSubjectType(UUID.randomUUID(), StatisticsSubjectType.COO);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void save_ShouldCreateNewStatistics() {
        // Arrange
        UUID replyId = UUID.randomUUID();
        Statistics newStatistics = new Statistics();
        newStatistics.setSubjectId(replyId);
        newStatistics.setSubjectType(StatisticsSubjectType.REPLY);
        newStatistics.setRepliesCount(5);
        newStatistics.setLikesCount(15);
        newStatistics.setViewsCount(25);
        newStatistics.setUpdatedAt(OffsetDateTime.now());

        // Act
        Statistics savedStatistics = statisticsRepository.save(newStatistics);

        // Assert
        assertNotNull(savedStatistics.getId());
        assertEquals(replyId, savedStatistics.getSubjectId());
        assertEquals(StatisticsSubjectType.REPLY, savedStatistics.getSubjectType());
        assertEquals(5, savedStatistics.getRepliesCount());
        assertEquals(15, savedStatistics.getLikesCount());
        assertEquals(25, savedStatistics.getViewsCount());
    }

    @Test
    void save_ShouldUpdateExistingStatistics() {
        // Arrange
        Statistics existingStatistics = statisticsRepository.findBySubjectIdAndSubjectType(cooId, StatisticsSubjectType.COO).orElseThrow();
        existingStatistics.setRepliesCount(15);
        existingStatistics.setLikesCount(25);
        existingStatistics.setViewsCount(35);

        // Act
        Statistics updatedStatistics = statisticsRepository.save(existingStatistics);

        // Assert
        assertEquals(statisticsId, updatedStatistics.getId());
        assertEquals(15, updatedStatistics.getRepliesCount());
        assertEquals(25, updatedStatistics.getLikesCount());
        assertEquals(35, updatedStatistics.getViewsCount());
    }
}