package com.zzang.chongdae.comment.service.dto;

import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import java.time.LocalDateTime;

public record CommentLatestResponse(String content, LocalDateTime createdAt) {

    public CommentLatestResponse(CommentEntity comment) {
        this(comment.getContent(), comment.getCreatedAt());
    }
}
