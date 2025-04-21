package com.xsh.personalize.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class TimelineActivity {
    private UUID id;
    private UUID cooId;
    private String userHandle;
    private UUID userId;
    private String content;
    private OffsetDateTime createdAt;
    private UUID repliedToUserId;
    private String repliedToUserHandle;
    private UUID repliedToReplyId;
    private TimelineActivityType type;

    public TimelineActivity() {
    }

    public TimelineActivity(UUID id, UUID cooId, String userHandle, UUID userId, String content, OffsetDateTime createdAt,
                        UUID repliedToUserId, String repliedToUserHandle, UUID repliedToReplyId, TimelineActivityType type) {
        this.id = id;
        this.cooId = cooId;
        this.userHandle = userHandle;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.repliedToUserId = repliedToUserId;
        this.repliedToUserHandle = repliedToUserHandle;
        this.repliedToReplyId = repliedToReplyId;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCooId() {
        return cooId;
    }

    public void setCooId(UUID cooId) {
        this.cooId = cooId;
    }

    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getRepliedToUserId() {
        return repliedToUserId;
    }

    public void setRepliedToUserId(UUID repliedToUserId) {
        this.repliedToUserId = repliedToUserId;
    }

    public String getRepliedToUserHandle() {
        return repliedToUserHandle;
    }

    public void setRepliedToUserHandle(String repliedToUserHandle) {
        this.repliedToUserHandle = repliedToUserHandle;
    }

    public UUID getRepliedToReplyId() {
        return repliedToReplyId;
    }

    public void setRepliedToReplyId(UUID repliedToReplyId) {
        this.repliedToReplyId = repliedToReplyId;
    }

    public TimelineActivityType getType() {
        return type;
    }

    public void setType(TimelineActivityType type) {
        this.type = type;
    }
}