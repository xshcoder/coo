package com.xsh.reply.repository;

import com.xsh.reply.model.Reply;
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
@Import(ReplyRepository.class)
@AutoConfigureEmbeddedDatabase
@Sql("/test-data.sql")
class ReplyRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReplyRepository replyRepository;

    private UUID userId;
    private UUID cooId;
    private UUID replyId;
    private UUID replyToReplyId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        cooId = UUID.randomUUID();
        replyId = UUID.randomUUID();
        replyToReplyId = UUID.randomUUID();

        // Insert test user
        jdbcTemplate.update(
            "INSERT INTO users (id, handle, name, email, bio, created_at) VALUES (?, ?, ?, ?, ?, ?)",
            userId, "test_user", "Test User", "test@example.com", "test bio", OffsetDateTime.now()
        );

        // Insert test coo
        jdbcTemplate.update(
            "INSERT INTO coos (id, user_id, content, created_at) VALUES (?, ?, ?, ?)",
            cooId, userId, "Test coo content", OffsetDateTime.now()
        );

        // Insert test replies
        jdbcTemplate.update(
            "INSERT INTO replies (id, coo_id, user_id, content, replied_to_user_id, created_at) VALUES (?, ?, ?, ?, ?, ?)",
            replyId, cooId, userId, "Test reply content", userId, OffsetDateTime.now()
        );

        jdbcTemplate.update(
            "INSERT INTO replies (id, coo_id, user_id, content, replied_to_user_id, replied_to_reply_id, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
            replyToReplyId, cooId, userId, "Reply to reply content", userId, replyId, OffsetDateTime.now()
        );
        
    }

    @Test
    void findById_WhenReplyExists_ShouldReturnReply() {
        // Act
        Optional<Reply> result = replyRepository.findById(replyId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test reply content", result.get().getContent());
        assertEquals(cooId, result.get().getCooId());
        assertEquals(userId, result.get().getUserId());
    }

    @Test
    void findById_WhenReplyDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<Reply> result = replyRepository.findById(UUID.randomUUID());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByCooIdOrderByCreatedAtDesc_ShouldReturnRepliesWithPagination() {
        // Act
        Page<Reply> result = replyRepository.findByCooIdOrderByCreatedAtDesc(cooId, PageRequest.of(0, 10));

        // Assert
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream()
            .allMatch(reply -> reply.getCooId().equals(cooId)));
    }

    @Test
    void findByRepliedToReplyIdOrderByCreatedAtDesc_ShouldReturnRepliesWithPagination() {
        // Act
        Page<Reply> result = replyRepository.findByRepliedToReplyIdOrderByCreatedAtDesc(replyId, PageRequest.of(0, 10));

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals(replyToReplyId, result.getContent().get(0).getId());
        assertEquals(replyId, result.getContent().get(0).getRepliedToReplyId());
    }

    @Test
    void save_ShouldCreateNewReply() {
        // Arrange
        Reply newReply = new Reply();
        newReply.setCooId(cooId);
        newReply.setUserId(userId);
        newReply.setContent("New reply content");
        newReply.setRepliedToUserId(userId);

        // Act
        Reply savedReply = replyRepository.save(newReply);

        // Assert
        assertNotNull(savedReply.getId());
        assertEquals(cooId, savedReply.getCooId());
        assertEquals("New reply content", savedReply.getContent());
        
        // Verify statistics were updated
        Integer repliesCount = jdbcTemplate.queryForObject(
            "SELECT replies_count FROM statistics WHERE subject_id = ? AND subject_type = 'COO'",
            Integer.class,
            cooId
        );
        assertNotNull(repliesCount);
        assertTrue(repliesCount > 0);
    }
    
    @Test
    void save_ShouldCreateNewReplyToReply() {
        // Arrange
        Reply newReply = new Reply();
        newReply.setCooId(cooId);
        newReply.setUserId(userId);
        newReply.setContent("New reply to reply content");
        newReply.setRepliedToUserId(userId);
        newReply.setRepliedToReplyId(replyId);

        // Act
        Reply savedReply = replyRepository.save(newReply);

        // Assert
        assertNotNull(savedReply.getId());
        assertEquals(replyId, savedReply.getRepliedToReplyId());
        
        // Verify statistics were updated
        Integer repliesCount = jdbcTemplate.queryForObject(
            "SELECT replies_count FROM statistics WHERE subject_id = ? AND subject_type = 'REPLY'",
            Integer.class,
            replyId
        );
        assertNotNull(repliesCount);
        assertTrue(repliesCount > 0);
    }

    @Test
    void save_ShouldUpdateExistingReply() {
        // Arrange
        Reply existingReply = replyRepository.findById(replyId).orElseThrow();
        existingReply.setContent("Updated content");

        // Act
        Reply updatedReply = replyRepository.save(existingReply);

        // Assert
        assertEquals(replyId, updatedReply.getId());
        assertEquals("Updated content", updatedReply.getContent());
    }
    
    @Test
    void deleteById_ShouldRemoveReply() {
        // Act
        replyRepository.deleteById(replyId);
        
        // Assert
        Optional<Reply> result = replyRepository.findById(replyId);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void deleteById_ShouldUpdateStatisticsForCooReply() {
        // Arrange
        // First create a statistics entry for the coo
        UUID statId = UUID.randomUUID();
        jdbcTemplate.update(
            "INSERT INTO statistics (id, subject_id, subject_type, replies_count) VALUES (?, ?, 'COO', ?)",
            statId, cooId, 5
        );
        
        // Act
        replyRepository.deleteById(replyId);
        
        // Assert
        Integer repliesCount = jdbcTemplate.queryForObject(
            "SELECT replies_count FROM statistics WHERE subject_id = ? AND subject_type = 'COO'",
            Integer.class,
            cooId
        );
        assertNotNull(repliesCount);
        assertEquals(4, repliesCount);
    }
    
    @Test
    void deleteById_ShouldUpdateStatisticsForReplyToReply() {
        // Arrange
        // First create a statistics entry for the reply
        UUID statId = UUID.randomUUID();
        jdbcTemplate.update(
            "INSERT INTO statistics (id, subject_id, subject_type, replies_count) VALUES (?, ?, 'REPLY', ?)",
            statId, replyId, 3
        );
        
        // Act
        replyRepository.deleteById(replyToReplyId);
        
        // Assert
        Integer repliesCount = jdbcTemplate.queryForObject(
            "SELECT replies_count FROM statistics WHERE subject_id = ? AND subject_type = 'REPLY'",
            Integer.class,
            replyId
        );
        assertNotNull(repliesCount);
        assertEquals(2, repliesCount);
    }
    
    @Test
    void deleteById_ShouldNotReduceStatisticsBelowZero() {
        // Arrange
        // First create a statistics entry with zero count
        UUID statId = UUID.randomUUID();
        jdbcTemplate.update(
            "INSERT INTO statistics (id, subject_id, subject_type, replies_count) VALUES (?, ?, 'COO', ?)",
            statId, cooId, 0
        );
        
        // Act
        replyRepository.deleteById(replyId);
        
        // Assert
        Integer repliesCount = jdbcTemplate.queryForObject(
            "SELECT replies_count FROM statistics WHERE subject_id = ? AND subject_type = 'COO'",
            Integer.class,
            cooId
        );
        assertNotNull(repliesCount);
        assertEquals(0, repliesCount);
    }
}