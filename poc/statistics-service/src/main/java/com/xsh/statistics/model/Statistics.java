package com.xsh.statistics.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Statistics {
    private UUID id;
    private UUID subjectId;
    private StatisticsSubjectType subjectType;
    private Integer repliesCount;
    private Integer likesCount;
    private Integer viewsCount;
    private OffsetDateTime updatedAt;

    // Default constructor
    public Statistics() {
    }

    // Parameterized constructor
    public Statistics(UUID id, UUID subjectId, StatisticsSubjectType subjectType, 
                     Integer repliesCount, Integer likesCount, Integer viewsCount, 
                     OffsetDateTime updatedAt) {
        this.id = id;
        this.subjectId = subjectId;
        this.subjectType = subjectType;
        this.repliesCount = repliesCount;
        this.likesCount = likesCount;
        this.viewsCount = viewsCount;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }

    public StatisticsSubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(StatisticsSubjectType subjectType) {
        this.subjectType = subjectType;
    }

    public Integer getRepliesCount() {
        return repliesCount;
    }

    public void setRepliesCount(Integer repliesCount) {
        this.repliesCount = repliesCount;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}