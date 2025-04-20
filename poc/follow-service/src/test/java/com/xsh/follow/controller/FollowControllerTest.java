package com.xsh.follow.controller;

import com.xsh.follow.model.Follow;
import com.xsh.follow.service.FollowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FollowController.class)
public class FollowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FollowService followService;

    private Follow testFollow;
    private UUID followerId;
    private UUID followedId;

    @BeforeEach
    void setUp() {
        followerId = UUID.randomUUID();
        followedId = UUID.randomUUID();
        testFollow = new Follow(followerId, followedId);
        testFollow.setFollowedAt(OffsetDateTime.now());
    }

    @Test
    void followUser_WithValidData_ReturnsFollow() throws Exception {
        when(followService.followUser(any(UUID.class), any(UUID.class)))
                .thenReturn(testFollow);

        mockMvc.perform(post("/api/follows/" + followerId + "/follow/" + followedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followerId").value(followerId.toString()))
                .andExpect(jsonPath("$.followedId").value(followedId.toString()));
    }

    @Test
    void unfollowUser_WithValidData_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/follows/" + followerId + "/unfollow/" + followedId))
                .andExpect(status().isOk());
    }

    @Test
    void getFollowers_WithValidUserId_ReturnsFollowersList() throws Exception {
        List<Follow> follows = Arrays.asList(testFollow);
        Page<Follow> followPage = new PageImpl<>(follows);

        when(followService.getFollowers(any(UUID.class), any()))
                .thenReturn(followPage);

        mockMvc.perform(get("/api/follows/" + followedId + "/followers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].followerId").value(followerId.toString()))
                .andExpect(jsonPath("$.content[0].followedId").value(followedId.toString()));
    }

    @Test
    void getFollowing_WithValidUserId_ReturnsFollowingList() throws Exception {
        List<Follow> follows = Arrays.asList(testFollow);
        Page<Follow> followPage = new PageImpl<>(follows);

        when(followService.getFollowing(any(UUID.class), any()))
                .thenReturn(followPage);

        mockMvc.perform(get("/api/follows/" + followerId + "/following")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].followerId").value(followerId.toString()))
                .andExpect(jsonPath("$.content[0].followedId").value(followedId.toString()));
    }

    @Test
    void getFollowersCount_WithValidUserId_ReturnsCount() throws Exception {
        when(followService.getFollowersCount(any(UUID.class)))
                .thenReturn(5L);

        mockMvc.perform(get("/api/follows/" + followedId + "/followers/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void getFollowingCount_WithValidUserId_ReturnsCount() throws Exception {
        when(followService.getFollowingCount(any(UUID.class)))
                .thenReturn(3L);

        mockMvc.perform(get("/api/follows/" + followerId + "/following/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    void isFollowing_WithValidData_ReturnsBoolean() throws Exception {
        when(followService.isFollowing(any(UUID.class), any(UUID.class)))
                .thenReturn(true);

        mockMvc.perform(get("/api/follows/" + followerId + "/is-following/" + followedId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}