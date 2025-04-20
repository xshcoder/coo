package com.xsh.reply.service;

import com.xsh.reply.model.Reply;
import com.xsh.reply.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReplyServiceTest {

    @Mock
    private ReplyRepository replyRepository;

    private ReplyService replyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        replyService = new ReplyService(replyRepository);
    }

    @Test
    void getReply_WhenReplyExists_ShouldReturnReply() {
        // Arrange
        UUID id = UUID.randomUUID();
        Reply expectedReply = new Reply();
        when(replyRepository.findById(id)).thenReturn(Optional.of(expectedReply));

        // Act
        Reply actualReply = replyService.getReply(id);

        // Assert
        assertEquals(expectedReply, actualReply);
        verify(replyRepository).findById(id);
    }

    @Test
    void getReply_WhenReplyDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(replyRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> replyService.getReply(id));
        verify(replyRepository).findById(id);
    }

    @Test
    void createReplyToCoo_ShouldSaveReply() {
        // Arrange
        UUID cooId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String content = "Test reply";
        Reply savedReply = new Reply();
        when(replyRepository.save(any(Reply.class))).thenReturn(savedReply);

        // Act
        Reply actualReply = replyService.createReplyToCoo(cooId, userId, content);

        // Assert
        assertEquals(savedReply, actualReply);
        verify(replyRepository).save(any(Reply.class));
    }

    @Test
    void createReplyToReply_WhenParentReplyExists_ShouldSaveReply() {
        // Arrange
        UUID parentReplyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String content = "Test reply to reply";
        
        Reply parentReply = new Reply();
        parentReply.setId(parentReplyId);
        parentReply.setCooId(UUID.randomUUID());
        parentReply.setUserId(UUID.randomUUID());
        
        Reply savedReply = new Reply();
        when(replyRepository.findById(parentReplyId)).thenReturn(Optional.of(parentReply));
        when(replyRepository.save(any(Reply.class))).thenReturn(savedReply);

        // Act
        Reply actualReply = replyService.createReplyToReply(parentReplyId, userId, content);

        // Assert
        assertEquals(savedReply, actualReply);
        verify(replyRepository).findById(parentReplyId);
        verify(replyRepository).save(any(Reply.class));
    }

    @Test
    void createReplyToReply_WhenParentReplyDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID parentReplyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String content = "Test reply to reply";
        when(replyRepository.findById(parentReplyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, 
            () -> replyService.createReplyToReply(parentReplyId, userId, content));
        verify(replyRepository).findById(parentReplyId);
        verify(replyRepository, never()).save(any());
    }

    @Test
    void getRepliesForCoo_ShouldReturnPageOfReplies() {
        // Arrange
        UUID cooId = UUID.randomUUID();
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        
        List<Reply> repliesList = Arrays.asList(new Reply(), new Reply());
        Page<Reply> expectedPage = new PageImpl<>(repliesList, pageable, repliesList.size());
        
        when(replyRepository.findByCooIdOrderByCreatedAtDesc(cooId, pageable)).thenReturn(expectedPage);
        
        // Act
        Page<Reply> actualPage = replyService.getRepliesForCoo(cooId, pageable);
        
        // Assert
        assertEquals(expectedPage, actualPage);
        verify(replyRepository).findByCooIdOrderByCreatedAtDesc(cooId, pageable);
    }

    @Test
    void getRepliesForReply_ShouldReturnPageOfReplies() {
        // Arrange
        UUID replyId = UUID.randomUUID();
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        
        List<Reply> repliesList = Arrays.asList(new Reply(), new Reply());
        Page<Reply> expectedPage = new PageImpl<>(repliesList, pageable, repliesList.size());
        
        when(replyRepository.findByRepliedToReplyIdOrderByCreatedAtDesc(replyId, pageable))
            .thenReturn(expectedPage);
        
        // Act
        Page<Reply> actualPage = replyService.getRepliesForReply(replyId, pageable);
        
        // Assert
        assertEquals(expectedPage, actualPage);
        verify(replyRepository).findByRepliedToReplyIdOrderByCreatedAtDesc(replyId, pageable);
    }
}