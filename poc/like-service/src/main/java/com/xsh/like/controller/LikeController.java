package com.xsh.like.controller;

import com.xsh.like.model.Like;
import com.xsh.like.service.LikeService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/coo/{cooId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Like> likeCoo(
            @PathVariable UUID cooId,
            @RequestParam UUID userId,
            @RequestParam UUID likedToUserId) {
        Like like = likeService.likeCoo(cooId, userId, likedToUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(like);
    }

    @PostMapping("/reply/{replyId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Like> likeReply(
            @PathVariable UUID replyId,
            @RequestParam UUID userId,
            @RequestParam UUID likedToUserId) {
        Like like = likeService.likeReply(replyId, userId, likedToUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(like);
    }

    @PostMapping("/coo/{cooId}/unlike")
    public ResponseEntity<Void> unlikeCoo(
            @PathVariable UUID cooId,
            @RequestParam UUID userId) {
        likeService.unlikeCoo(cooId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reply/{replyId}/unlike")
    public ResponseEntity<Void> unlikeReply(
            @PathVariable UUID replyId,
            @RequestParam UUID userId) {
        likeService.unlikeReply(replyId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/coo/{cooId}")
    public ResponseEntity<Page<Like>> getLikesByCooId(
            @PathVariable UUID cooId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Like> likes = likeService.getLikesByCooId(cooId, page, size);
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/reply/{replyId}")
    public ResponseEntity<Page<Like>> getLikesByReplyId(
            @PathVariable UUID replyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Like> likes = likeService.getLikesByReplyId(replyId, page, size);
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Like>> getLikesByUserId(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Like> likes = likeService.getLikesByUserId(userId, page, size);
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/count/coo/{cooId}")
    public ResponseEntity<Map<String, Integer>> getLikesCountForCoo(@PathVariable UUID cooId) {
        int count = likeService.getLikesCountForCoo(cooId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/count/reply/{replyId}")
    public ResponseEntity<Map<String, Integer>> getLikesCountForReply(@PathVariable UUID replyId) {
        int count = likeService.getLikesCountForReply(replyId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/status/coo/{cooId}")
    public ResponseEntity<Map<String, Boolean>> hasUserLikedCoo(
            @PathVariable UUID cooId,
            @RequestParam UUID userId) {
        boolean hasLiked = likeService.hasUserLikedCoo(cooId, userId);
        return ResponseEntity.ok(Map.of("hasLiked", hasLiked));
    }

    @GetMapping("/status/reply/{replyId}")
    public ResponseEntity<Map<String, Boolean>> hasUserLikedReply(
            @PathVariable UUID replyId,
            @RequestParam UUID userId) {
        boolean hasLiked = likeService.hasUserLikedReply(replyId, userId);
        return ResponseEntity.ok(Map.of("hasLiked", hasLiked));
    }
}