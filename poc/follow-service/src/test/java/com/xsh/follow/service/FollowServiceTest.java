package com.xsh.follow.service;

import com.xsh.follow.model.Follow;
import com.xsh.follow.repository.FollowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private FollowService followService;

    private UUID userId1;
    private UUID userId2;
    private UUID userId3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();
        userId3 = UUID.randomUUID();
    }

    @Test
    void followUser_Success() {
        // Arrange
        Follow follow = new Follow(userId1, userId2);

        when(followRepository.exists(userId1, userId2)).thenReturn(false);
        when(followRepository.save(any(Follow.class))).thenReturn(follow);

        // Act
        Follow result = followService.followUser(userId1, userId2);

        // Assert
        assertNotNull(result);
        assertEquals(userId1, result.getFollowerId());
        assertEquals(userId2, result.getFollowedId());
        verify(followRepository).save(any(Follow.class));
    }

    @Test
    void unfollowUser_Success() {
        // Arrange
        when(followRepository.exists(userId1, userId2)).thenReturn(true);

        // Act
        followService.unfollowUser(userId1, userId2);

        // Assert
        verify(followRepository).delete(userId1, userId2);
    }

    @Test
    void getFollowers_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Follow follow1 = new Follow(userId2, userId1);
        Follow follow2 = new Follow(userId3, userId1);

        Page<Follow> followPage = new PageImpl<>(Arrays.asList(follow1, follow2));
        when(followRepository.findFollowersByUserId(userId1, pageable)).thenReturn(followPage);

        // Act
        Page<Follow> result = followService.getFollowers(userId1, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(followRepository).findFollowersByUserId(userId1, pageable);
    }

    @Test
    void getFollowing_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Follow follow1 = new Follow(userId1, userId2);
        Follow follow2 = new Follow(userId1, userId3);

        Page<Follow> followPage = new PageImpl<>(Arrays.asList(follow1, follow2));
        when(followRepository.findFollowingByUserId(userId1, pageable)).thenReturn(followPage);

        // Act
        Page<Follow> result = followService.getFollowing(userId1, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(followRepository).findFollowingByUserId(userId1, pageable);
    }

    @Test
    void getFollowersCount_Success() {
        // Arrange
        when(followRepository.getFollowersCount(userId1)).thenReturn(5L);

        // Act
        long count = followService.getFollowersCount(userId1);

        // Assert
        assertEquals(5L, count);
        verify(followRepository).getFollowersCount(userId1);
    }

    @Test
    void getFollowingCount_Success() {
        // Arrange
        when(followRepository.getFollowingCount(userId1)).thenReturn(3L);

        // Act
        long count = followService.getFollowingCount(userId1);

        // Assert
        assertEquals(3L, count);
        verify(followRepository).getFollowingCount(userId1);
    }
}