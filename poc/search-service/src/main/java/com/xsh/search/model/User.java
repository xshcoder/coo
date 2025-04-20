package com.xsh.search.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class User {
    private UUID id;
    private String handle;
    private String name;
    private String email;
    private String bio;
    private OffsetDateTime createdAt;

    public User() {
    }

    public User(UUID id, String handle, String name, String email, String bio, OffsetDateTime createdAt) {
        this.id = id;
        this.handle = handle;
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}