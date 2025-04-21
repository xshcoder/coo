package com.xsh.personalize.repository;

import com.xsh.personalize.model.TimelineActivity;
import com.xsh.personalize.model.TimelineActivityType;
import com.xsh.personalize.model.TimelineCursor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class PersonalizeRepository {

    private final JdbcTemplate jdbcTemplate;

    public PersonalizeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<TimelineActivity> timelineRowMapper = (ResultSet rs, int rowNum) -> {
        TimelineActivity activity = new TimelineActivity();
        activity.setId(UUID.fromString(rs.getString("id")));
        activity.setCooId(UUID.fromString(rs.getString("coo_id")));
        activity.setUserId(UUID.fromString(rs.getString("user_id")));
        activity.setUserHandle(rs.getString("user_handle"));
        activity.setContent(rs.getString("content"));
        activity.setCreatedAt(rs.getObject("created_at", java.time.OffsetDateTime.class));
        
        String repliedToUserId = rs.getString("replied_to_user_id");
        if (repliedToUserId != null) {
            activity.setRepliedToUserId(UUID.fromString(repliedToUserId));
        }
        
        activity.setRepliedToUserHandle(rs.getString("replied_to_user_handle"));
        
        String repliedToReplyId = rs.getString("replied_to_reply_id");
        if (repliedToReplyId != null) {
            activity.setRepliedToReplyId(UUID.fromString(repliedToReplyId));
        }
        
        activity.setType(TimelineActivityType.valueOf(rs.getString("type")));
        return activity;
    };

    public TimelineCursor getTimeline(UUID userId, String cursor, int limit) {
        OffsetDateTime cursorTimestamp = cursor != null ? OffsetDateTime.parse(cursor) : OffsetDateTime.now();
        
        String coosSql = """
            SELECT 
                c.id,
                c.id as coo_id,
                c.user_id,
                u.handle as user_handle,
                c.content,
                c.created_at,
                NULL as replied_to_user_id,
                NULL as replied_to_user_handle,
                NULL as replied_to_reply_id,
                'Coo' as type
            FROM coos c
            JOIN users u ON c.user_id = u.id
            JOIN follows f ON c.user_id = f.followed_id
            WHERE f.follower_id = ? AND c.created_at < ?
            ORDER BY c.created_at DESC
            LIMIT ?
            """;

        String repliesSql = """
            SELECT 
                r.id,
                r.coo_id,
                r.user_id,
                u.handle as user_handle,
                r.content,
                r.created_at,
                r.replied_to_user_id,
                u2.handle as replied_to_user_handle,
                r.replied_to_reply_id,
                'Reply' as type
            FROM replies r
            JOIN users u ON r.user_id = u.id
            LEFT JOIN users u2 ON r.replied_to_user_id = u2.id
            JOIN coos c ON r.coo_id = c.id
            WHERE c.user_id = ? AND r.created_at < ?
            ORDER BY r.created_at DESC
            LIMIT ?
            """;

        List<TimelineActivity> coos = jdbcTemplate.query(
                coosSql,
                timelineRowMapper,
                userId,
                cursorTimestamp,
                limit
        );

        List<TimelineActivity> replies = jdbcTemplate.query(
                repliesSql,
                timelineRowMapper,
                userId,
                cursorTimestamp,
                limit
        );

        List<TimelineActivity> merged = Stream.concat(coos.stream(), replies.stream())
                .sorted(Comparator.comparing(TimelineActivity::getCreatedAt).reversed())
                .limit(limit)
                .collect(Collectors.toList());

        boolean hasMore = coos.size() + replies.size() > limit;
        OffsetDateTime lastTimestamp = merged.isEmpty() ? null : merged.get(merged.size() - 1).getCreatedAt();

        return new TimelineCursor(merged, lastTimestamp, hasMore);
    }
}