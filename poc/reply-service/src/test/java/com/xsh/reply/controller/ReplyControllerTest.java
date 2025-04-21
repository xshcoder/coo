package com.xsh.reply.controller;

import com.xsh.reply.model.Reply;
import com.xsh.reply.service.ReplyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReplyController.class)
class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReplyService replyService;

    private UUID cooId;
    private UUID userId;
    private UUID replyId;
    private Reply testReply;

    @BeforeEach
    void setUp() {
        cooId = UUID.randomUUID();
        userId = UUID.randomUUID();
        replyId = UUID.randomUUID();

        testReply = new Reply();
        testReply.setId(replyId);
        testReply.setCooId(cooId);
        testReply.setUserId(userId);
        testReply.setContent("Test reply content");
        testReply.setCreatedAt(OffsetDateTime.now());
    }

    @Test
    void replyToCoo_ShouldReturnCreatedReply() throws Exception {
        when(replyService.createReplyToCoo(eq(cooId), eq(userId), any(String.class)))
            .thenReturn(testReply);

        mockMvc.perform(post("/api/replies/coo/{cooId}", cooId)
                .param("userId", userId.toString())
                .content("Test reply content")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(replyId.toString()))
                .andExpect(jsonPath("$.content").value("Test reply content"));
    }

    @Test
    void replyToReply_ShouldReturnCreatedReply() throws Exception {
        Reply replyToReply = new Reply();
        replyToReply.setId(UUID.randomUUID());
        replyToReply.setCooId(cooId);
        replyToReply.setUserId(userId);
        replyToReply.setContent("Reply to reply content");
        replyToReply.setRepliedToReplyId(replyId);

        when(replyService.createReplyToReply(eq(replyId), eq(userId), any(String.class)))
            .thenReturn(replyToReply);

        mockMvc.perform(post("/api/replies/reply/{replyId}", replyId)
                .param("userId", userId.toString())
                .content("Reply to reply content")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repliedToReplyId").value(replyId.toString()));
    }

    @Test
    void getReply_ShouldReturnReply() throws Exception {
        when(replyService.getReply(replyId)).thenReturn(testReply);

        mockMvc.perform(get("/api/replies/{replyId}", replyId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(replyId.toString()));
    }

    @Test
    void getRepliesForCoo_ShouldReturnPageOfReplies() throws Exception {
        Page<Reply> replyPage = new PageImpl<>(Arrays.asList(testReply));
        when(replyService.getRepliesForCoo(eq(cooId), any(PageRequest.class)))
            .thenReturn(replyPage);

        mockMvc.perform(get("/api/replies/coo/{cooId}", cooId)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(replyId.toString()));
    }

    @Test
    void getRepliesForReply_ShouldReturnPageOfReplies() throws Exception {
        Page<Reply> replyPage = new PageImpl<>(Arrays.asList(testReply));
        when(replyService.getRepliesForReply(eq(replyId), any(PageRequest.class)))
            .thenReturn(replyPage);

        mockMvc.perform(get("/api/replies/reply/{replyId}", replyId)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(replyId.toString()));
    }
}