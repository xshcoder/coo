package com.xsh.reply.controller;

import com.xsh.reply.model.Reply;
import com.xsh.reply.service.ReplyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/replies")
public class ReplyController {
    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/coo/{cooId}")
    public ResponseEntity<Reply> replyToCoo(
            @PathVariable UUID cooId,
            @RequestParam UUID userId,
            @RequestBody String content) {
        Reply reply = replyService.createReplyToCoo(cooId, userId, content);
        return ResponseEntity.ok(reply);
    }

    @PostMapping("/reply/{replyId}")
    public ResponseEntity<Reply> replyToReply(
            @PathVariable UUID replyId,
            @RequestParam UUID userId,
            @RequestBody String content) {
        Reply reply = replyService.createReplyToReply(replyId, userId, content);
        return ResponseEntity.ok(reply);
    }

    @GetMapping("/coo/{cooId}")
    public ResponseEntity<Page<Reply>> getRepliesForCoo(
            @PathVariable UUID cooId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Reply> replies = replyService.getRepliesForCoo(cooId, PageRequest.of(page, size));
        return ResponseEntity.ok(replies);
    }

    @GetMapping("/{replyId}")
    public ResponseEntity<Reply> getReply(@PathVariable UUID replyId) {
        Reply reply = replyService.getReply(replyId);
        return ResponseEntity.ok(reply);
    }

    @GetMapping("/reply/{replyId}")
    public ResponseEntity<Page<Reply>> getRepliesForReply(
            @PathVariable UUID replyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Reply> replies = replyService.getRepliesForReply(replyId, PageRequest.of(page, size));
        return ResponseEntity.ok(replies);
    }
    
    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(
            @PathVariable UUID replyId,
            @RequestParam UUID userId) {
        replyService.deleteReply(replyId, userId);
        return ResponseEntity.noContent().build();
    }
}