package com.xsh.reply.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
}