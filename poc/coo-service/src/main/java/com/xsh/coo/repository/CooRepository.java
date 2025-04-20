package com.xsh.coo.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.xsh.coo.model.Coo;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CooRepository {

    private final JdbcTemplate jdbcTemplate;

    public CooRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Coo> cooRowMapper = (ResultSet rs, int rowNum) -> {
        Coo coo = new Coo();
        coo.setId(UUID.fromString(rs.getString("id")));
        coo.setUserId(UUID.fromString(rs.getString("user_id")));
        coo.setContent(rs.getString("content"));
        coo.setCreatedAt(rs.getObject("created_at", java.time.OffsetDateTime.class));
        return coo;
    };

    public Optional<Coo> findById(UUID id) {
        List<Coo> results = jdbcTemplate.query(
                "SELECT * FROM coos WHERE id = ?",
                cooRowMapper,
                id
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Page<Coo> findByUserId(UUID userId, Pageable pageable) {
        String sql = "SELECT * FROM coos WHERE user_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Coo> content = jdbcTemplate.query(
                sql,
                cooRowMapper,
                userId,
                pageable.getPageSize(),
                pageable.getOffset()
        );
        
        String countSql = "SELECT COUNT(*) FROM coos WHERE user_id = ?";
        int total = jdbcTemplate.queryForObject(countSql, Integer.class, userId);
        
        return new PageImpl<>(content, pageable, total);
    }

    public Coo save(Coo coo) {
        if (coo.getId() == null) {
            // Insert new coo
            UUID id = UUID.randomUUID();
            jdbcTemplate.update(
                    "INSERT INTO coos (id, user_id, content) VALUES (?, ?, ?)",
                    id,
                    coo.getUserId(),
                    coo.getContent()
            );
            coo.setId(id);
            return findById(id).orElseThrow();
        } else {
            // Update existing coo
            jdbcTemplate.update(
                    "UPDATE coos SET content = ? WHERE id = ?",
                    coo.getContent(),
                    coo.getId()
            );
            return findById(coo.getId()).orElseThrow();
        }
    }

    public void deleteById(UUID id) {
        jdbcTemplate.update("DELETE FROM coos WHERE id = ?", id);
    }
}