package com.xsh.follow.repository;

import com.xsh.follow.model.Follow;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FollowRepository {
    private final JdbcTemplate jdbcTemplate;

    public FollowRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Follow> followRowMapper = (ResultSet rs, int rowNum) -> {
        Follow follow = new Follow();
        follow.setId(UUID.fromString(rs.getString("id")));
        follow.setFollowerId(UUID.fromString(rs.getString("follower_id")));
        follow.setFollowedId(UUID.fromString(rs.getString("followed_id")));
        follow.setFollowedAt(rs.getObject("followed_at", java.time.OffsetDateTime.class));
        return follow;
    };

    public Follow save(Follow follow) {
        if (follow.getId() == null) {
            // Insert new follow
            UUID id = UUID.randomUUID();
            String sql = "INSERT INTO follows (id, follower_id, followed_id, followed_at) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    id,
                    follow.getFollowerId(),
                    follow.getFollowedId(),
                    follow.getFollowedAt());
            follow.setId(id);
            return follow;
        } else {
            // Update existing follow (not typically needed for follows)
            String sql = "UPDATE follows SET followed_at = ? WHERE id = ?";
            jdbcTemplate.update(sql, follow.getFollowedAt(), follow.getId());
            return follow;
        }
    }

    public void delete(UUID followerId, UUID followedId) {
        String sql = "DELETE FROM follows WHERE follower_id = ? AND followed_id = ?";
        jdbcTemplate.update(sql, followerId, followedId);
    }
    
    public Optional<Follow> findById(UUID id) {
        String sql = "SELECT * FROM follows WHERE id = ?";
        List<Follow> results = jdbcTemplate.query(sql, followRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public boolean exists(UUID followerId, UUID followedId) {
        String sql = "SELECT COUNT(*) FROM follows WHERE follower_id = ? AND followed_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, followerId, followedId);
        return count > 0;
    }

    public Page<Follow> findFollowersByUserId(UUID userId, Pageable pageable) {
        String sql = "SELECT * FROM follows WHERE followed_id = ? ORDER BY followed_at DESC LIMIT ? OFFSET ?";
        List<Follow> content = jdbcTemplate.query(sql, followRowMapper, 
                userId, 
                pageable.getPageSize(), 
                pageable.getOffset());
        
        String countSql = "SELECT COUNT(*) FROM follows WHERE followed_id = ?";
        Long total = jdbcTemplate.queryForObject(countSql, Long.class, userId);
        
        return new PageImpl<>(content, pageable, total);
    }

    public Page<Follow> findFollowingByUserId(UUID userId, Pageable pageable) {
        String sql = "SELECT * FROM follows WHERE follower_id = ? ORDER BY followed_at DESC LIMIT ? OFFSET ?";
        List<Follow> content = jdbcTemplate.query(sql, followRowMapper, 
                userId, 
                pageable.getPageSize(), 
                pageable.getOffset());
        
        String countSql = "SELECT COUNT(*) FROM follows WHERE follower_id = ?";
        Long total = jdbcTemplate.queryForObject(countSql, Long.class, userId);
        
        return new PageImpl<>(content, pageable, total);
    }

    public Long getFollowersCount(UUID userId) {
        String sql = "SELECT COUNT(*) FROM follows WHERE followed_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, userId);
    }

    public Long getFollowingCount(UUID userId) {
        String sql = "SELECT COUNT(*) FROM follows WHERE follower_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, userId);
    }
}