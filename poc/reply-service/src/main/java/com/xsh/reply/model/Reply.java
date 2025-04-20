package com.xsh.reply.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Reply {
    private UUID id;
    private UUID cooId;
    private UUID userId;
    private String content;
    private OffsetDateTime createdAt;
    private UUID repliedToUserId;
    private UUID repliedToReplyId;

    public Reply() {
    }

    public Reply(UUID id, UUID cooId, UUID userId, String content, OffsetDateTime createdAt, UUID repliedToUserId, UUID repliedToReplyId) {
        this.id = id;
        this.cooId = cooId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.repliedToUserId = repliedToUserId;
        this.repliedToReplyId = repliedToReplyId;
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

    public UUID getRepliedToReplyId() {
        return repliedToReplyId;
    }

    public void setRepliedToReplyId(UUID repliedToReplyId) {
        this.repliedToReplyId = repliedToReplyId;
    }
}