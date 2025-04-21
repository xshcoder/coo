package com.xsh.personalize.controller;

import com.xsh.personalize.model.TimelineActivity;
import com.xsh.personalize.model.TimelineCursor;
import com.xsh.personalize.service.PersonalizeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.Base64;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonalizeController.class)
public class PersonalizeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonalizeService personalizeService;

    private TimelineActivity testActivity;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testActivity = new TimelineActivity();
        testActivity.setId(UUID.randomUUID());
        testActivity.setCooId(UUID.randomUUID());
        testActivity.setUserId(userId);
        testActivity.setUserHandle("test_user");
        testActivity.setContent("Test content");
        testActivity.setCreatedAt(OffsetDateTime.now());
    }

    @Test
    void getTimeline_WithValidUserId_ReturnsTimelineCursor() throws Exception {
        TimelineCursor timelineCursor = new TimelineCursor(
            Arrays.asList(testActivity),
            testActivity.getCreatedAt(),
            false
        );

        String encodedCursor = Base64.getEncoder().encodeToString(OffsetDateTime.now().toString().getBytes());

        when(personalizeService.getTimeline(any(UUID.class), anyString(), anyInt()))
            .thenReturn(timelineCursor);

        mockMvc.perform(get("/api/personalize/timeline/" + userId)
                .param("cursor", encodedCursor)
                .param("limit", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.activities[0].content").value("Test content"))
            .andExpect(jsonPath("$.activities[0].userHandle").value("test_user"));
    }

    @Test
    void getTimeline_WithoutCursor_ReturnsTimelineCursor() throws Exception {
        TimelineCursor timelineCursor = new TimelineCursor(
            Arrays.asList(testActivity),
            testActivity.getCreatedAt(),
            false
        );

        when(personalizeService.getTimeline(any(UUID.class), isNull(), anyInt()))
            .thenReturn(timelineCursor);

        mockMvc.perform(get("/api/personalize/timeline/" + userId)
                .param("limit", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.activities[0].content").value("Test content"));
    }

    @Test
    void getTimeline_WithDefaultLimit_ReturnsTimelineCursor() throws Exception {
        TimelineCursor timelineCursor = new TimelineCursor(
            Arrays.asList(testActivity),
            testActivity.getCreatedAt(),
            false
        );

        when(personalizeService.getTimeline(any(UUID.class), isNull(), eq(10)))
            .thenReturn(timelineCursor);

        mockMvc.perform(get("/api/personalize/timeline/" + userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.activities[0].content").value("Test content"));
    }
}