package com.xsh.like.service;

import com.xsh.like.model.Like;
import com.xsh.like.repository.LikeRepository;
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

class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    private LikeService likeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        likeService = new LikeService(likeRepository);
    }

    @Test
    void likeCoo_WhenNotAlreadyLiked_ShouldSaveLike() {
        // Arrange
        UUID cooId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID likedToUserId = UUID.randomUUID();
        
        when(likeRepository.findByCooIdAndUserId(cooId, userId)).thenReturn(Optional.empty());
        
        Like savedLike = new Like();
        when(likeRepository.save(any(Like.class))).thenReturn(savedLike);
        
        // Act
        Like result = likeService.likeCoo(cooId, userId, likedToUserId);
        
        // Assert
        assertEquals(savedLike, result);
        verify(likeRepository).findByCooIdAndUserId(cooId, userId);
        verify(likeRepository).save(any(Like.class));
    }
    
    @Test
    void likeCoo_WhenAlreadyLiked_ShouldThrowException() {
        // Arrange
        UUID cooId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID likedToUserId = UUID.randomUUID();
        
        when(likeRepository.findByCooIdAndUserId(cooId, userId)).thenReturn(Optional.of(new Like()));
        
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> likeService.likeCoo(cooId, userId, likedToUserId));
        verify(likeRepository).findByCooIdAndUserId(cooId, userId);
        verify(likeRepository, never()).save(any());
    }
    
    @Test
    void likeReply_WhenNotAlreadyLiked_ShouldSaveLike() {
        // Arrange
        UUID replyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID likedToUserId = UUID.randomUUID();
        
        when(likeRepository.findByReplyIdAndUserId(replyId, userId)).thenReturn(Optional.empty());
        
        Like savedLike = new Like();
        when(likeRepository.save(any(Like.class))).thenReturn(savedLike);
        
        // Act
        Like result = likeService.likeReply(replyId, userId, likedToUserId);
        
        // Assert
        assertEquals(savedLike, result);
        verify(likeRepository).findByReplyIdAndUserId(replyId, userId);
        verify(likeRepository).save(any(Like.class));
    }
    
    @Test
    void likeReply_WhenAlreadyLiked_ShouldThrowException() {
        // Arrange
        UUID replyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID likedToUserId = UUID.randomUUID();
        
        when(likeRepository.findByReplyIdAndUserId(replyId, userId)).thenReturn(Optional.of(new Like()));
        
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> likeService.likeReply(replyId, userId, likedToUserId));
        verify(likeRepository).findByReplyIdAndUserId(replyId, userId);
        verify(likeRepository, never()).save(any());
    }
    
    @Test
    void unlikeCoo_WhenLikeExists_ShouldDeleteLike() {
        // Arrange
        UUID cooId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        Like like = new Like();
        UUID likeId = UUID.randomUUID();
        like.setId(likeId);
        
        when(likeRepository.findByCooIdAndUserId(cooId, userId)).thenReturn(Optional.of(like));
        
        // Act
        likeService.unlikeCoo(cooId, userId);
        
        // Assert
        verify(likeRepository).findByCooIdAndUserId(cooId, userId);
        verify(likeRepository).delete(likeId);
    }
    
    @Test
    void unlikeCoo_WhenLikeDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID cooId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        when(likeRepository.findByCooIdAndUserId(cooId, userId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> likeService.unlikeCoo(cooId, userId));
        verify(likeRepository).findByCooIdAndUserId(cooId, userId);
        verify(likeRepository, never()).delete(any());
    }
    
    @Test
    void unlikeReply_WhenLikeExists_ShouldDeleteLike() {
        // Arrange
        UUID replyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        Like like = new Like();
        UUID likeId = UUID.randomUUID();
        like.setId(likeId);
        
        when(likeRepository.findByReplyIdAndUserId(replyId, userId)).thenReturn(Optional.of(like));
        
        // Act
        likeService.unlikeReply(replyId, userId);
        
        // Assert
        verify(likeRepository).findByReplyIdAndUserId(replyId, userId);
        verify(likeRepository).delete(likeId);
    }
    
    @Test
    void unlikeReply_WhenLikeDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID replyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        when(likeRepository.findByReplyIdAndUserId(replyId, userId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> likeService.unlikeReply(replyId, userId));
        verify(likeRepository).findByReplyIdAndUserId(replyId, userId);
        verify(likeRepository, never()).delete(any());
    }
    
    @Test
    void getLikesByCooId_ShouldReturnPageOfLikes() {
        // Arrange
        UUID cooId = UUID.randomUUID();
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        
        List<Like> likesList = Arrays.asList(new Like(), new Like());
        Page<Like> expectedPage = new PageImpl<>(likesList, pageable, likesList.size());
        
        when(likeRepository.findByCooId(cooId, pageable)).thenReturn(expectedPage);
        
        // Act
        Page<Like> actualPage = likeService.getLikesByCooId(cooId, page, size);
        
        // Assert
        assertEquals(expectedPage, actualPage);
        verify(likeRepository).findByCooId(cooId, pageable);
    }
    
    @Test
    void getLikesByReplyId_ShouldReturnPageOfLikes() {
        // Arrange
        UUID replyId = UUID.randomUUID();
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        
        List<Like> likesList = Arrays.asList(new Like(), new Like());
        Page<Like> expectedPage = new PageImpl<>(likesList, pageable, likesList.size());
        
        when(likeRepository.findByReplyId(replyId, pageable)).thenReturn(expectedPage);
        
        // Act
        Page<Like> actualPage = likeService.getLikesByReplyId(replyId, page, size);
        
        // Assert
        assertEquals(expectedPage, actualPage);
        verify(likeRepository).findByReplyId(replyId, pageable);
    }
    
    @Test
    void getLikesCountForCoo_ShouldReturnCount() {
        // Arrange
        UUID cooId = UUID.randomUUID();
        int expectedCount = 5;
        
        when(likeRepository.countByCooId(cooId)).thenReturn(expectedCount);
        
        // Act
        int actualCount = likeService.getLikesCountForCoo(cooId);
        
        // Assert
        assertEquals(expectedCount, actualCount);
        verify(likeRepository).countByCooId(cooId);
    }
    
    @Test
    void hasUserLikedCoo_WhenLikeExists_ShouldReturnTrue() {
        // Arrange
        UUID cooId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        when(likeRepository.findByCooIdAndUserId(cooId, userId)).thenReturn(Optional.of(new Like()));
        
        // Act
        boolean result = likeService.hasUserLikedCoo(cooId, userId);
        
        // Assert
        assertTrue(result);
        verify(likeRepository).findByCooIdAndUserId(cooId, userId);
    }
    
    @Test
    void hasUserLikedCoo_WhenLikeDoesNotExist_ShouldReturnFalse() {
        // Arrange
        UUID cooId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        when(likeRepository.findByCooIdAndUserId(cooId, userId)).thenReturn(Optional.empty());
        
        // Act
        boolean result = likeService.hasUserLikedCoo(cooId, userId);
        
        // Assert
        assertFalse(result);
        verify(likeRepository).findByCooIdAndUserId(cooId, userId);
    }
}