package com.xsh.like.service;

import com.xsh.like.model.Like;
import com.xsh.like.repository.LikeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public Like likeCoo(UUID cooId, UUID userId, UUID likedToUserId) {
        // Check if already liked
        if (likeRepository.findByCooIdAndUserId(cooId, userId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has already liked this coo");
        }

        Like like = new Like();
        like.setCooId(cooId);
        like.setUserId(userId);
        like.setLikedToUserId(likedToUserId);
        return likeRepository.save(like);
    }

    public Like likeReply(UUID replyId, UUID userId, UUID likedToUserId) {
        // Check if already liked
        if (likeRepository.findByReplyIdAndUserId(replyId, userId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has already liked this reply");
        }

        Like like = new Like();
        like.setReplyId(replyId);
        like.setUserId(userId);
        like.setLikedToUserId(likedToUserId);
        return likeRepository.save(like);
    }

    public void unlikeCoo(UUID cooId, UUID userId) {
        Like like = likeRepository.findByCooIdAndUserId(cooId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Like not found"));
        likeRepository.delete(like.getId());
    }

    public void unlikeReply(UUID replyId, UUID userId) {
        Like like = likeRepository.findByReplyIdAndUserId(replyId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Like not found"));
        likeRepository.delete(like.getId());
    }

    public Page<Like> getLikesByCooId(UUID cooId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return likeRepository.findByCooId(cooId, pageable);
    }

    public Page<Like> getLikesByReplyId(UUID replyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return likeRepository.findByReplyId(replyId, pageable);
    }

    public Page<Like> getLikesByUserId(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return likeRepository.findByUserId(userId, pageable);
    }

    public int getLikesCountForCoo(UUID cooId) {
        return likeRepository.countByCooId(cooId);
    }

    public int getLikesCountForReply(UUID replyId) {
        return likeRepository.countByReplyId(replyId);
    }

    public boolean hasUserLikedCoo(UUID cooId, UUID userId) {
        return likeRepository.findByCooIdAndUserId(cooId, userId).isPresent();
    }

    public boolean hasUserLikedReply(UUID replyId, UUID userId) {
        return likeRepository.findByReplyIdAndUserId(replyId, userId).isPresent();
    }
}