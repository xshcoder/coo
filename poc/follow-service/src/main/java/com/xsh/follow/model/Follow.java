package com.xsh.follow.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Follow {
    private UUID id;
    private UUID followerId;
    private UUID followedId;
    private OffsetDateTime followedAt;

    public Follow() {}

    public Follow(UUID followerId, UUID followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
        this.followedAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFollowerId() {
        return followerId;
    }

    public void setFollowerId(UUID followerId) {
        this.followerId = followerId;
    }

    public UUID getFollowedId() {
        return followedId;
    }

    public void setFollowedId(UUID followedId) {
        this.followedId = followedId;
    }

    public OffsetDateTime getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(OffsetDateTime followedAt) {
        this.followedAt = followedAt;
    }
}