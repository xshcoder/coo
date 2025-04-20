package com.xsh.search.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.xsh.search.model.User;
import com.xsh.search.model.Coo;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

@Repository
public class SearchRepository {

    private final JdbcTemplate jdbcTemplate;

    public SearchRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (ResultSet rs, int rowNum) -> {
        User user = new User();
        user.setId(UUID.fromString(rs.getString("id")));
        user.setHandle(rs.getString("handle"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setBio(rs.getString("bio"));
        user.setCreatedAt(rs.getObject("created_at", java.time.OffsetDateTime.class));
        return user;
    };

    private final RowMapper<Coo> cooRowMapper = (ResultSet rs, int rowNum) -> {
        Coo coo = new Coo();
        coo.setId(UUID.fromString(rs.getString("id")));
        coo.setUserId(UUID.fromString(rs.getString("user_id")));
        coo.setContent(rs.getString("content"));
        coo.setCreatedAt(rs.getObject("created_at", java.time.OffsetDateTime.class));
        return coo;
    };

    public Page<User> searchUsers(String text, Pageable pageable) {
        String sql = "SELECT * FROM users WHERE LOWER(handle) LIKE LOWER(?) OR LOWER(name) LIKE LOWER(?) " +
                     "ORDER BY created_at DESC LIMIT ? OFFSET ?";
        String searchPattern = "%" + text + "%";
        
        List<User> content = jdbcTemplate.query(
                sql,
                userRowMapper,
                searchPattern,
                searchPattern,
                pageable.getPageSize(),
                pageable.getOffset()
        );
        
        String countSql = "SELECT COUNT(*) FROM users WHERE LOWER(handle) LIKE LOWER(?) OR LOWER(name) LIKE LOWER(?)";
        int total = jdbcTemplate.queryForObject(countSql, Integer.class, searchPattern, searchPattern);
        
        return new PageImpl<>(content, pageable, total);
    }

    public Page<Coo> searchCoos(String query, Pageable pageable) {
        String sql = "SELECT * FROM coos WHERE LOWER(content) LIKE LOWER(?) " +
                     "ORDER BY created_at DESC LIMIT ? OFFSET ?";
        String searchPattern = "%" + query + "%";
        
        List<Coo> content = jdbcTemplate.query(
                sql,
                cooRowMapper,
                searchPattern,
                pageable.getPageSize(),
                pageable.getOffset()
        );
        
        String countSql = "SELECT COUNT(*) FROM coos WHERE LOWER(content) LIKE LOWER(?)";
        int total = jdbcTemplate.queryForObject(countSql, Integer.class, searchPattern);
        
        return new PageImpl<>(content, pageable, total);
    }
}