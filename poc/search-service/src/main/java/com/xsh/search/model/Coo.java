package com.xsh.search.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Coo {
    private UUID id;
    private UUID userId;
    private String content;
    private OffsetDateTime createdAt;

    public Coo() {
    }

    public Coo(UUID id, UUID userId, String content, OffsetDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}