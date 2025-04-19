package com.xsh.userservice.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.xsh.userservice.model.User;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
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

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", userRowMapper);
    }

    public Optional<User> findById(UUID id) {
        List<User> results = jdbcTemplate.query(
                "SELECT * FROM users WHERE id = ?",
                userRowMapper,
                id
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Optional<User> findByHandle(String handle) {
        List<User> results = jdbcTemplate.query(
                "SELECT * FROM users WHERE handle = ?",
                userRowMapper,
                handle
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public User save(User user) {
        if (user.getId() == null) {
            // Insert new user
            UUID id = UUID.randomUUID();
            jdbcTemplate.update(
                    "INSERT INTO users (id, handle, name, email, bio) VALUES (?, ?, ?, ?, ?)",
                    id,
                    user.getHandle(),
                    user.getName(),
                    user.getEmail(),
                    user.getBio()
            );
            user.setId(id);
            return findById(id).orElseThrow();
        } else {
            // Update existing user
            jdbcTemplate.update(
                    "UPDATE users SET handle = ?, name = ?, email = ?, bio = ? WHERE id = ?",
                    user.getHandle(),
                    user.getName(),
                    user.getEmail(),
                    user.getBio(),
                    user.getId()
            );
            return findById(user.getId()).orElseThrow();
        }
    }

    public void deleteById(UUID id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }
}