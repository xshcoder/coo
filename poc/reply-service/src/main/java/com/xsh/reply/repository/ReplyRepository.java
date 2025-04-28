package com.xsh.reply.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.xsh.reply.model.Reply;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ReplyRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReplyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reply> replyRowMapper = (ResultSet rs, int rowNum) -> {
        Reply reply = new Reply();
        reply.setId(UUID.fromString(rs.getString("id")));
        reply.setCooId(UUID.fromString(rs.getString("coo_id")));
        reply.setUserId(UUID.fromString(rs.getString("user_id")));
        reply.setContent(rs.getString("content"));
        reply.setCreatedAt(rs.getObject("created_at", java.time.OffsetDateTime.class));
        String repliedToUserId = rs.getString("replied_to_user_id");
        if (repliedToUserId != null) {
            reply.setRepliedToUserId(UUID.fromString(repliedToUserId));
        }
        String repliedToReplyId = rs.getString("replied_to_reply_id");
        if (repliedToReplyId != null) {
            reply.setRepliedToReplyId(UUID.fromString(repliedToReplyId));
        }
        return reply;
    };

    public Optional<Reply> findById(UUID id) {
        List<Reply> results = jdbcTemplate.query(
                "SELECT * FROM replies WHERE id = ?",
                replyRowMapper,
                id
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Page<Reply> findByCooIdOrderByCreatedAtDesc(UUID cooId, Pageable pageable) {
        String sql = "SELECT * FROM replies WHERE coo_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Reply> content = jdbcTemplate.query(
                sql,
                replyRowMapper,
                cooId,
                pageable.getPageSize(),
                pageable.getOffset()
        );
        
        String countSql = "SELECT COUNT(*) FROM replies WHERE coo_id = ?";
        int total = jdbcTemplate.queryForObject(countSql, Integer.class, cooId);
        
        return new PageImpl<>(content, pageable, total);
    }

    public Page<Reply> findByRepliedToReplyIdOrderByCreatedAtDesc(UUID replyId, Pageable pageable) {
        String sql = "SELECT * FROM replies WHERE replied_to_reply_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Reply> content = jdbcTemplate.query(
                sql,
                replyRowMapper,
                replyId,
                pageable.getPageSize(),
                pageable.getOffset()
        );
        
        String countSql = "SELECT COUNT(*) FROM replies WHERE replied_to_reply_id = ?";
        int total = jdbcTemplate.queryForObject(countSql, Integer.class, replyId);
        
        return new PageImpl<>(content, pageable, total);
    }

    @Transactional
    public Reply save(Reply reply) {
        if (reply.getId() == null) {
            // Insert new reply
            UUID id = UUID.randomUUID();
            jdbcTemplate.update(
                    "INSERT INTO replies (id, coo_id, user_id, content, replied_to_user_id, replied_to_reply_id) VALUES (?, ?, ?, ?, ?, ?)",
                    id,
                    reply.getCooId(),
                    reply.getUserId(),
                    reply.getContent(),
                    reply.getRepliedToUserId(),
                    reply.getRepliedToReplyId()
            );
            reply.setId(id);
            
            // Update statistics - increment replies_count
            if (reply.getRepliedToReplyId() != null) {
                // This is a reply to another reply
                jdbcTemplate.update(
                        "INSERT INTO statistics (id, subject_id, subject_type, replies_count) VALUES (?, ?, 'REPLY', 1) " +
                        "ON CONFLICT (subject_id, subject_type) DO UPDATE SET replies_count = statistics.replies_count + 1",
                        UUID.randomUUID(), // Generate a new ID for the statistics row, since we don't have one for the CO
                        reply.getRepliedToReplyId()
                );
            } else {
                // This is a reply to a coo
                jdbcTemplate.update(
                        "INSERT INTO statistics (id, subject_id, subject_type, replies_count) VALUES (?, ?, 'COO', 1) " +
                        "ON CONFLICT (subject_id, subject_type) DO UPDATE SET replies_count = statistics.replies_count + 1",
                        UUID.randomUUID(), // Generate a new ID for the statistics row, since we don't have one for the CO
                        reply.getCooId()
                );
            }
            
            return findById(id).orElseThrow();
        } else {
            // Update existing reply
            jdbcTemplate.update(
                    "UPDATE replies SET content = ? WHERE id = ?",
                    reply.getContent(),
                    reply.getId()
            );
            return findById(reply.getId()).orElseThrow();
        }
    }
    
    @Transactional
    public void deleteById(UUID id) {
        // Get the reply before deleting to know what statistics to update
        Optional<Reply> replyOptional = findById(id);
        if (replyOptional.isPresent()) {
            Reply reply = replyOptional.get();
            
            // Delete the reply
            jdbcTemplate.update("DELETE FROM replies WHERE id = ?", id);
            
            // Update statistics - decrement replies_count
            if (reply.getRepliedToReplyId() != null) {
                // This was a reply to another reply
                jdbcTemplate.update(
                        "UPDATE statistics SET replies_count = replies_count - 1 " +
                        "WHERE subject_id = ? AND subject_type = 'REPLY' AND replies_count > 0",
                        reply.getRepliedToReplyId()
                );
            } else {
                // This was a reply to a coo
                jdbcTemplate.update(
                        "UPDATE statistics SET replies_count = replies_count - 1 " +
                        "WHERE subject_id = ? AND subject_type = 'COO' AND replies_count > 0",
                        reply.getCooId()
                );
            }
        } else {
            // If reply doesn't exist, just try to delete anyway
            jdbcTemplate.update("DELETE FROM replies WHERE id = ?", id);
        }
    }
}