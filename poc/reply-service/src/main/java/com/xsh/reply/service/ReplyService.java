package com.xsh.reply.service;

import com.xsh.reply.model.Reply;
import com.xsh.reply.repository.ReplyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Service
public class ReplyService {
    private final ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public Reply createReplyToCoo(UUID cooId, UUID userId, String content) {
        Reply reply = new Reply();
        reply.setCooId(cooId);
        reply.setUserId(userId);
        reply.setContent(content);
        return replyRepository.save(reply);
    }

    public Reply createReplyToReply(UUID replyId, UUID userId, String content) {
        Reply parentReply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found with id: " + replyId));

        Reply reply = new Reply();
        reply.setCooId(parentReply.getCooId());
        reply.setUserId(userId);
        reply.setContent(content);
        reply.setRepliedToUserId(parentReply.getUserId());
        reply.setRepliedToReplyId(replyId);
        return replyRepository.save(reply);
    }

    public Reply getReply(UUID replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found with id: " + replyId));
    }

    public Page<Reply> getRepliesForCoo(UUID cooId, Pageable pageable) {
        return replyRepository.findByCooIdOrderByCreatedAtDesc(cooId, pageable);
    }

    public Page<Reply> getRepliesForReply(UUID replyId, Pageable pageable) {
        return replyRepository.findByRepliedToReplyIdOrderByCreatedAtDesc(replyId, pageable);
    }
    
    public void deleteReply(UUID replyId, UUID userId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found with id: " + replyId));
        
        if (!reply.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to delete this reply");
        }
        
        replyRepository.deleteById(replyId);
    }
}