package com.xsh.personalize.repository;

import com.xsh.personalize.model.TimelineActivity;
import com.xsh.personalize.model.TimelineCursor;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(PersonalizeRepository.class)
@AutoConfigureEmbeddedDatabase
@Sql("/test-data.sql")
class PersonalizeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PersonalizeRepository personalizeRepository;

    private UUID userId;
    private UUID followedUserId;
    private UUID cooId;
    private UUID replyId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        followedUserId = UUID.randomUUID();
        cooId = UUID.randomUUID();
        replyId = UUID.randomUUID();

        // Insert test users
        jdbcTemplate.update(
            "INSERT INTO users (id, handle, name, email, bio, created_at) VALUES (?, ?, ?, ?, ?, ?)",
            userId, "test_user", "Test User", "test@example.com", "test bio", OffsetDateTime.now()
        );
        jdbcTemplate.update(
            "INSERT INTO users (id, handle, name, email, bio, created_at) VALUES (?, ?, ?, ?, ?, ?)",
            followedUserId, "followed_user", "Followed User", "followed@example.com", "followed bio", OffsetDateTime.now()
        );

        // Insert follow relationship
        jdbcTemplate.update(
            "INSERT INTO follows (follower_id, followed_id, created_at) VALUES (?, ?, ?)",
            userId, followedUserId, OffsetDateTime.now()
        );

        // Insert test coo
        jdbcTemplate.update(
            "INSERT INTO coos (id, user_id, content, created_at) VALUES (?, ?, ?, ?)",
            cooId, followedUserId, "Test coo content", OffsetDateTime.now()
        );

        // Insert test reply
        jdbcTemplate.update(
            "INSERT INTO replies (id, coo_id, user_id, content, replied_to_user_id, created_at) VALUES (?, ?, ?, ?, ?, ?)",
            replyId, cooId, userId, "Test reply content", followedUserId, OffsetDateTime.now()
        );
    }

    @Test
    void getTimeline_WithValidInput_ShouldReturnTimelineCursor() {
        // Act
        TimelineCursor result = personalizeRepository.getTimeline(userId, null, 10);

        // Assert
        assertNotNull(result);
        assertFalse(result.getActivities().isEmpty());
        
        // Verify coo in timeline
        TimelineActivity coo = result.getActivities().stream()
            .filter(a -> a.getId().equals(cooId))
            .findFirst()
            .orElse(null);
        assertNotNull(coo);
        assertEquals("Test coo content", coo.getContent());
        assertEquals(followedUserId, coo.getUserId());
        assertEquals("followed_user", coo.getUserHandle());

        // Verify reply in timeline
        TimelineActivity reply = result.getActivities().stream()
            .filter(a -> a.getId().equals(replyId))
            .findFirst()
            .orElse(null);
        assertNull(reply);
    }

    @Test
    void getTimeline_WithCursor_ShouldReturnTimelineAfterCursor() {
        // Arrange
        OffsetDateTime cursor = OffsetDateTime.now().minusHours(1);

        // Act
        TimelineCursor result = personalizeRepository.getTimeline(userId, cursor.toString(), 10);

        // Assert
        assertNotNull(result);
        assertTrue(result.getActivities().isEmpty());
        assertTrue(result.getActivities().stream()
            .allMatch(activity -> activity.getCreatedAt().isAfter(cursor)));
    }

    @Test
    void getTimeline_WithLimit_ShouldReturnLimitedResults() {
        // Act
        TimelineCursor result = personalizeRepository.getTimeline(userId, null, 1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getActivities().size());
        assertFalse(result.isHasMore());
    }
}