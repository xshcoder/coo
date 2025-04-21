package com.xsh.like.controller;

import com.xsh.like.model.Like;
import com.xsh.like.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LikeController.class)
public class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    private Like testLike;
    private UUID userId;
    private UUID likedToUserId;
    private UUID cooId;
    private UUID replyId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        likedToUserId = UUID.randomUUID();
        cooId = UUID.randomUUID();
        replyId = UUID.randomUUID();

        testLike = new Like();
        testLike.setId(UUID.randomUUID());
        testLike.setUserId(userId);
        testLike.setLikedToUserId(likedToUserId);
        testLike.setLikedAt(OffsetDateTime.now());
    }

    @Test
    void likeCoo_WithValidData_ReturnsCreatedLike() throws Exception {
        testLike.setCooId(cooId);
        when(likeService.likeCoo(any(UUID.class), any(UUID.class), any(UUID.class)))
                .thenReturn(testLike);

        mockMvc.perform(post("/api/likes/coo/" + cooId)
                        .param("userId", userId.toString())
                        .param("likedToUserId", likedToUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cooId").value(cooId.toString()))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.likedToUserId").value(likedToUserId.toString()));
    }

    @Test
    void likeReply_WithValidData_ReturnsCreatedLike() throws Exception {
        testLike.setReplyId(replyId);
        when(likeService.likeReply(any(UUID.class), any(UUID.class), any(UUID.class)))
                .thenReturn(testLike);

        mockMvc.perform(post("/api/likes/reply/" + replyId)
                        .param("userId", userId.toString())
                        .param("likedToUserId", likedToUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.replyId").value(replyId.toString()))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.likedToUserId").value(likedToUserId.toString()));
    }

    @Test
    void unlikeCoo_WithValidData_ReturnsNoContent() throws Exception {
        mockMvc.perform(post("/api/likes/coo/" + cooId + "/unlike")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void unlikeReply_WithValidData_ReturnsNoContent() throws Exception {
        mockMvc.perform(post("/api/likes/reply/" + replyId + "/unlike")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getLikesByCooId_WithValidData_ReturnsLikesList() throws Exception {
        List<Like> likes = Arrays.asList(testLike);
        Page<Like> likePage = new PageImpl<>(likes);

        when(likeService.getLikesByCooId(any(UUID.class), anyInt(), anyInt()))
                .thenReturn(likePage);

        mockMvc.perform(get("/api/likes/coo/" + cooId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].userId").value(userId.toString()));
    }

    @Test
    void getLikesByReplyId_WithValidData_ReturnsLikesList() throws Exception {
        List<Like> likes = Arrays.asList(testLike);
        Page<Like> likePage = new PageImpl<>(likes);

        when(likeService.getLikesByReplyId(any(UUID.class), anyInt(), anyInt()))
                .thenReturn(likePage);

        mockMvc.perform(get("/api/likes/reply/" + replyId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].userId").value(userId.toString()));
    }
}