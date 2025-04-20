package com.xsh.like.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.xsh.like.model.Like;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class LikeRepository {

    private final JdbcTemplate jdbcTemplate;

    public LikeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Like> likeRowMapper = (ResultSet rs, int rowNum) -> {
        Like like = new Like();
        like.setId(UUID.fromString(rs.getString("id")));
        
        String cooId = rs.getString("coo_id");
        if (cooId != null) {
            like.setCooId(UUID.fromString(cooId));
        }
        
        String replyId = rs.getString("reply_id");
        if (replyId != null) {
            like.setReplyId(UUID.fromString(replyId));
        }
        
        like.setUserId(UUID.fromString(rs.getString("user_id")));
        
        String likedToUserId = rs.getString("liked_to_user_id");
        if (likedToUserId != null) {
            like.setLikedToUserId(UUID.fromString(likedToUserId));
        }
        
        like.setLikedAt(rs.getObject("liked_at", java.time.OffsetDateTime.class));
        return like;
    };

    public Optional<Like> findById(UUID id) {
        List<Like> results = jdbcTemplate.query(
                "SELECT * FROM likes WHERE id = ?",
                likeRowMapper,
                id
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Optional<Like> findByCooIdAndUserId(UUID cooId, UUID userId) {
        List<Like> results = jdbcTemplate.query(
                "SELECT * FROM likes WHERE coo_id = ? AND user_id = ?",
                likeRowMapper,
                cooId,
                userId
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Optional<Like> findByReplyIdAndUserId(UUID replyId, UUID userId) {
        List<Like> results = jdbcTemplate.query(
                "SELECT * FROM likes WHERE reply_id = ? AND user_id = ?",
                likeRowMapper,
                replyId,
                userId
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Page<Like> findByCooId(UUID cooId, Pageable pageable) {
        String sql = "SELECT * FROM likes WHERE coo_id = ? ORDER BY liked_at DESC LIMIT ? OFFSET ?";
        List<Like> content = jdbcTemplate.query(
                sql,
                likeRowMapper,
                cooId,
                pageable.getPageSize(),
                pageable.getOffset()
        );
        
        String countSql = "SELECT COUNT(*) FROM likes WHERE coo_id = ?";
        int total = jdbcTemplate.queryForObject(countSql, Integer.class, cooId);
        
        return new PageImpl<>(content, pageable, total);
    }

    public Page<Like> findByReplyId(UUID replyId, Pageable pageable) {
        String sql = "SELECT * FROM likes WHERE reply_id = ? ORDER BY liked_at DESC LIMIT ? OFFSET ?";
        List<Like> content = jdbcTemplate.query(
                sql,
                likeRowMapper,
                replyId,
                pageable.getPageSize(),
                pageable.getOffset()
        );
        
        String countSql = "SELECT COUNT(*) FROM likes WHERE reply_id = ?";
        int total = jdbcTemplate.queryForObject(countSql, Integer.class, replyId);
        
        return new PageImpl<>(content, pageable, total);
    }

    public Page<Like> findByUserId(UUID userId, Pageable pageable) {
        String sql = "SELECT * FROM likes WHERE user_id = ? ORDER BY liked_at DESC LIMIT ? OFFSET ?";
        List<Like> content = jdbcTemplate.query(
                sql,
                likeRowMapper,
                userId,
                pageable.getPageSize(),
                pageable.getOffset()
        );
        
        String countSql = "SELECT COUNT(*) FROM likes WHERE user_id = ?";
        int total = jdbcTemplate.queryForObject(countSql, Integer.class, userId);
        
        return new PageImpl<>(content, pageable, total);
    }

    public Like save(Like like) {
        if (like.getId() == null) {
            // Insert new like
            UUID id = UUID.randomUUID();
            jdbcTemplate.update(
                    "INSERT INTO likes (id, coo_id, reply_id, user_id, liked_to_user_id) VALUES (?, ?, ?, ?, ?)",
                    id,
                    like.getCooId(),
                    like.getReplyId(),
                    like.getUserId(),
                    like.getLikedToUserId()
            );
            like.setId(id);
            return findById(id).orElseThrow();
        }
        return like;
    }

    public void delete(UUID id) {
        jdbcTemplate.update("DELETE FROM likes WHERE id = ?", id);
    }

    public int countByCooId(UUID cooId) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM likes WHERE coo_id = ?",
                Integer.class,
                cooId
        );
    }

    public int countByReplyId(UUID replyId) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM likes WHERE reply_id = ?",
                Integer.class,
                replyId
        );
    }
}