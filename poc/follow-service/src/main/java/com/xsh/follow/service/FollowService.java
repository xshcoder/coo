package com.xsh.follow.service;

import com.xsh.follow.model.Follow;
import com.xsh.follow.repository.FollowRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FollowService {
    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public Follow followUser(UUID followerId, UUID followedId) {
        if (followerId.equals(followedId)) {
            throw new IllegalArgumentException("Users cannot follow themselves");
        }

        if (followRepository.exists(followerId, followedId)) {
            throw new IllegalStateException("Already following this user");
        }

        Follow follow = new Follow(followerId, followedId);
        return followRepository.save(follow);
    }

    public void unfollowUser(UUID followerId, UUID followedId) {
        if (!followRepository.exists(followerId, followedId)) {
            throw new IllegalStateException("Not following this user");
        }
        followRepository.delete(followerId, followedId);
    }

    public Page<Follow> getFollowers(UUID userId, Pageable pageable) {
        return followRepository.findFollowersByUserId(userId, pageable);
    }

    public Page<Follow> getFollowing(UUID userId, Pageable pageable) {
        return followRepository.findFollowingByUserId(userId, pageable);
    }

    public long getFollowersCount(UUID userId) {
        return followRepository.getFollowersCount(userId);
    }

    public long getFollowingCount(UUID userId) {
        return followRepository.getFollowingCount(userId);
    }

    public boolean isFollowing(UUID followerId, UUID followedId) {
        return followRepository.exists(followerId, followedId);
    }
}