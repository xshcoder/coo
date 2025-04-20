package com.xsh.follow.controller;

import com.xsh.follow.model.Follow;
import com.xsh.follow.service.FollowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/follows")
public class FollowController {
    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{followerId}/follow/{followedId}")
    public ResponseEntity<Follow> followUser(
            @PathVariable UUID followerId,
            @PathVariable UUID followedId) {
        Follow follow = followService.followUser(followerId, followedId);
        return ResponseEntity.ok(follow);
    }

    @DeleteMapping("/{followerId}/unfollow/{followedId}")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable UUID followerId,
            @PathVariable UUID followedId) {
        followService.unfollowUser(followerId, followedId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<Page<Follow>> getFollowers(
            @PathVariable UUID userId,
            Pageable pageable) {
        Page<Follow> followers = followService.getFollowers(userId, pageable);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<Page<Follow>> getFollowing(
            @PathVariable UUID userId,
            Pageable pageable) {
        Page<Follow> following = followService.getFollowing(userId, pageable);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Long> getFollowersCount(@PathVariable UUID userId) {
        long count = followService.getFollowersCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Long> getFollowingCount(@PathVariable UUID userId) {
        long count = followService.getFollowingCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{followerId}/is-following/{followedId}")
    public ResponseEntity<Boolean> isFollowing(
            @PathVariable UUID followerId,
            @PathVariable UUID followedId) {
        boolean isFollowing = followService.isFollowing(followerId, followedId);
        return ResponseEntity.ok(isFollowing);
    }
}