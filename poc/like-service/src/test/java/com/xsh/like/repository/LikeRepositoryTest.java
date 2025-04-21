package com.xsh.like.repository;

import com.xsh.like.model.Like;
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
@Import(LikeRepository.class)
@AutoConfigureEmbeddedDatabase
@Sql("/test-data.sql")
class LikeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LikeRepository likeRepository;

    private UUID userId;
    private UUID likedToUserId;
    private UUID cooId;
    private UUID replyId;
    private UUID likeId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        likedToUserId = UUID.randomUUID();
        cooId = UUID.randomUUID();
        replyId = UUID.randomUUID();
        likeId = UUID.randomUUID();

        // Insert test users
        jdbcTemplate.update(
            "INSERT INTO users (id, handle, name, email, bio, created_at) VALUES (?, ?, ?, ?, ?, ?)",
            userId, "test_user", "Test User", "test@example.com", "test bio", OffsetDateTime.now()
        );
        jdbcTemplate.update(
            "INSERT INTO users (id, handle, name, email, bio, created_at) VALUES (?, ?, ?, ?, ?, ?)",
            likedToUserId, "liked_user", "Liked User", "liked@example.com", "liked bio", OffsetDateTime.now()
        );

        // Insert test coo
        jdbcTemplate.update(
            "INSERT INTO coos (id, user_id, content, created_at) VALUES (?, ?, ?, ?)",
            cooId, likedToUserId, "Test coo", OffsetDateTime.now()
        );

        // Insert test reply
        jdbcTemplate.update(
            "INSERT INTO replies (id, coo_id, user_id, content, created_at) VALUES (?, ?, ?, ?, ?)",
            replyId, cooId, likedToUserId, "Test reply", OffsetDateTime.now()
        );
    }

    @Test
    void likeCoo_ShouldCreateLikeForCoo() {
        // Arrange
        Like newLike = new Like();
        newLike.setCooId(cooId);
        newLike.setUserId(userId);
        newLike.setLikedToUserId(likedToUserId);

        // Act
        Like result = likeRepository.save(newLike);

        // Assert
        assertNotNull(result);
        assertEquals(cooId, result.getCooId());
        assertEquals(userId, result.getUserId());
        assertEquals(likedToUserId, result.getLikedToUserId());
        assertNull(result.getReplyId());
    }

    @Test
    void likeReply_ShouldCreateLikeForReply() {
        // Arrange
        Like newLike = new Like();
        newLike.setReplyId(replyId);
        newLike.setUserId(userId);
        newLike.setLikedToUserId(likedToUserId);

        // Act
        Like result = likeRepository.save(newLike);

        // Assert
        assertNotNull(result);
        assertEquals(replyId, result.getReplyId());
        assertEquals(userId, result.getUserId());
        assertEquals(likedToUserId, result.getLikedToUserId());
        assertNull(result.getCooId());
    }

    @Test
    void findByCooIdAndUserId_ShouldReturnLike() {
        // Arrange
        Like like = new Like();
        like.setCooId(cooId);
        like.setUserId(userId);
        like.setLikedToUserId(likedToUserId);
        likeRepository.save(like);

        // Act
        Optional<Like> result = likeRepository.findByCooIdAndUserId(cooId, userId);
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(cooId, result.get().getCooId());
        assertEquals(userId, result.get().getUserId());
    }

    @Test
    void findByReplyIdAndUserId_ShouldReturnLike() {
        // Arrange
        Like like = new Like();
        like.setReplyId(replyId);
        like.setUserId(userId);
        like.setLikedToUserId(likedToUserId);
        likeRepository.save(like);

        // Act
        Optional<Like> result = likeRepository.findByReplyIdAndUserId(replyId, userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(replyId, result.get().getReplyId());
        assertEquals(userId, result.get().getUserId());
    }

    @Test
    void deleteByCooIdAndUserId_ShouldRemoveLike() {
        // Arrange
        Like like = new Like();
        like.setCooId(cooId);
        like.setUserId(userId);
        like.setLikedToUserId(likedToUserId);
        likeRepository.save(like);

        // Act
        Optional<Like> likeToDelete = likeRepository.findByCooIdAndUserId(cooId, userId);
        likeRepository.delete(likeToDelete.get().getId());

        // Assert
        Optional<Like> result = likeRepository.findByCooIdAndUserId(cooId, userId);
        assertFalse(result.isPresent());
    }

    @Test
    void deleteByReplyIdAndUserId_ShouldRemoveLike() {
        // Arrange
        Like like = new Like();
        like.setReplyId(replyId);
        like.setUserId(userId);
        like.setLikedToUserId(likedToUserId);
        likeRepository.save(like);

        // Act
        Optional<Like> likeToDelete = likeRepository.findByReplyIdAndUserId(replyId, userId);
        likeRepository.delete(likeToDelete.get().getId());

        // Assert
        Optional<Like> result = likeRepository.findByReplyIdAndUserId(replyId, userId);
        assertFalse(result.isPresent());
    }
}