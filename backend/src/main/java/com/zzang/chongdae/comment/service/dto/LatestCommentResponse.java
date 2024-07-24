package com.zzang.chongdae.comment.service.dto;

import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import java.time.LocalDateTime;

public record LatestCommentResponse(String content, LocalDateTime createdAt) {

    public LatestCommentResponse(CommentEntity comment) {
        this(comment.getContent(), comment.getCreatedAt());
    }
}
