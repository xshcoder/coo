package com.xsh.like.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Like {
    private UUID id;
    private UUID cooId;
    private UUID replyId;
    private UUID userId;
    private UUID likedToUserId;
    private OffsetDateTime likedAt;

    public Like() {
    }

    public Like(UUID id, UUID cooId, UUID replyId, UUID userId, UUID likedToUserId, OffsetDateTime likedAt) {
        this.id = id;
        this.cooId = cooId;
        this.replyId = replyId;
        this.userId = userId;
        this.likedToUserId = likedToUserId;
        this.likedAt = likedAt;
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

    public UUID getReplyId() {
        return replyId;
    }

    public void setReplyId(UUID replyId) {
        this.replyId = replyId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getLikedToUserId() {
        return likedToUserId;
    }

    public void setLikedToUserId(UUID likedToUserId) {
        this.likedToUserId = likedToUserId;
    }

    public OffsetDateTime getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(OffsetDateTime likedAt) {
        this.likedAt = likedAt;
    }
}