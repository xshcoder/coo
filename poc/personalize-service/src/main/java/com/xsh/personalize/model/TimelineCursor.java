package com.xsh.personalize.model;

import java.time.OffsetDateTime;
import java.util.List;

public class TimelineCursor {
    private List<TimelineActivity> activities;
    private String nextCursor;
    private boolean hasMore;

    public TimelineCursor(List<TimelineActivity> activities, OffsetDateTime lastTimestamp, boolean hasMore) {
        this.activities = activities;
        this.nextCursor = lastTimestamp != null ? lastTimestamp.toString() : null;
        this.hasMore = hasMore;
    }

    public List<TimelineActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<TimelineActivity> activities) {
        this.activities = activities;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}