package com.xsh.statistics.repository;

import com.xsh.statistics.model.Statistics;
import com.xsh.statistics.model.StatisticsSubjectType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StatisticsRepository {

    private final JdbcTemplate jdbcTemplate;

    public StatisticsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Statistics> statisticsRowMapper = (ResultSet rs, int rowNum) -> {
        Statistics statistics = new Statistics();
        statistics.setId(UUID.fromString(rs.getString("id")));
        statistics.setSubjectId(UUID.fromString(rs.getString("subject_id")));
        statistics.setSubjectType(StatisticsSubjectType.valueOf(rs.getString("subject_type")));
        statistics.setRepliesCount(rs.getInt("replies_count"));
        statistics.setLikesCount(rs.getInt("likes_count"));
        statistics.setViewsCount(rs.getInt("views_count"));
        statistics.setUpdatedAt(rs.getObject("updated_at", OffsetDateTime.class));
        return statistics;
    };

    public Optional<Statistics> findBySubjectIdAndSubjectType(UUID subjectId, StatisticsSubjectType subjectType) {
        String sql = "SELECT * FROM statistics WHERE subject_id = ? AND subject_type = ?";
        List<Statistics> results = jdbcTemplate.query(
                sql,
                statisticsRowMapper,
                subjectId,
                subjectType.name()
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Statistics save(Statistics statistics) {
        if (statistics.getId() == null) {
            // Insert new statistics
            UUID id = UUID.randomUUID();
            jdbcTemplate.update(
                    "INSERT INTO statistics (id, subject_id, subject_type, replies_count, likes_count, views_count, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    id,
                    statistics.getSubjectId(),
                    statistics.getSubjectType().name(),
                    statistics.getRepliesCount(),
                    statistics.getLikesCount(),
                    statistics.getViewsCount(),
                    statistics.getUpdatedAt()
            );
            statistics.setId(id);
            return findById(id).orElse(statistics);
        } else {
            // Update existing statistics
            jdbcTemplate.update(
                    "UPDATE statistics SET replies_count = ?, likes_count = ?, views_count = ?, updated_at = ? " +
                    "WHERE id = ?",
                    statistics.getRepliesCount(),
                    statistics.getLikesCount(),
                    statistics.getViewsCount(),
                    statistics.getUpdatedAt(),
                    statistics.getId()
            );
            return findById(statistics.getId()).orElse(statistics);
        }
    }

    public Optional<Statistics> findById(UUID id) {
        String sql = "SELECT * FROM statistics WHERE id = ?";
        List<Statistics> results = jdbcTemplate.query(
                sql,
                statisticsRowMapper,
                id
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<Statistics> findBySubjectIdsAndSubjectType(List<UUID> subjectIds, StatisticsSubjectType subjectType) {
        if (subjectIds == null || subjectIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Create a comma-separated string of question marks for the IN clause
        String placeholders = String.join(",", Collections.nCopies(subjectIds.size(), "?"));
        String sql = "SELECT * FROM statistics WHERE subject_id IN (" + placeholders + ") AND subject_type = ?";
        
        // Create an array of objects for the query parameters
        Object[] params = new Object[subjectIds.size() + 1];
        for (int i = 0; i < subjectIds.size(); i++) {
            params[i] = subjectIds.get(i);
        }
        params[subjectIds.size()] = subjectType.name();
        
        return jdbcTemplate.query(sql, statisticsRowMapper, params);
    }
}