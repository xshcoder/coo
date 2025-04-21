package com.xsh.personalize.service;

import com.xsh.personalize.model.TimelineActivity;
import com.xsh.personalize.model.TimelineCursor;
import com.xsh.personalize.repository.PersonalizeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class PersonalizeServiceTest {

    @Mock
    private PersonalizeRepository personalizeRepository;

    private PersonalizeService personalizeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        personalizeService = new PersonalizeService(personalizeRepository);
    }

    @Test
    void getTimeline_WithValidInput_ShouldReturnTimelineCursor() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String rawCursor = OffsetDateTime.now().toString();
        String encodedCursor = Base64.getEncoder().encodeToString(rawCursor.getBytes());
        int limit = 10;

        TimelineActivity activity = new TimelineActivity();
        activity.setId(UUID.randomUUID());
        activity.setCooId(UUID.randomUUID());
        activity.setUserId(userId);
        activity.setContent("Test content");
        activity.setCreatedAt(OffsetDateTime.now());

        TimelineCursor expectedCursor = new TimelineCursor(
            Arrays.asList(activity),
            activity.getCreatedAt(),
            false
        );

        when(personalizeRepository.getTimeline(any(UUID.class), eq(rawCursor), anyInt()))
            .thenReturn(expectedCursor);

        // Act
        TimelineCursor result = personalizeService.getTimeline(userId, encodedCursor, limit);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getActivities().size());
        assertEquals(expectedCursor.getNextCursor(), result.getNextCursor());
        assertEquals(expectedCursor.isHasMore(), result.isHasMore());
        
        TimelineActivity resultActivity = result.getActivities().get(0);
        assertEquals(activity.getId(), resultActivity.getId());
        assertEquals(activity.getCooId(), resultActivity.getCooId());
        assertEquals(activity.getUserId(), resultActivity.getUserId());
        assertEquals(activity.getContent(), resultActivity.getContent());
    }

    @Test
    void getTimeline_WithNullCursor_ShouldReturnTimelineCursor() {
        // Arrange
        UUID userId = UUID.randomUUID();
        int limit = 10;

        TimelineActivity activity = new TimelineActivity();
        activity.setId(UUID.randomUUID());
        activity.setCreatedAt(OffsetDateTime.now());

        TimelineCursor expectedCursor = new TimelineCursor(
            Arrays.asList(activity),
            activity.getCreatedAt(),
            false
        );

        when(personalizeRepository.getTimeline(any(UUID.class), isNull(), anyInt()))
            .thenReturn(expectedCursor);

        // Act
        TimelineCursor result = personalizeService.getTimeline(userId, null, limit);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getActivities().size());
        assertEquals(expectedCursor.getNextCursor(), result.getNextCursor());
        assertEquals(expectedCursor.isHasMore(), result.isHasMore());
    }
}