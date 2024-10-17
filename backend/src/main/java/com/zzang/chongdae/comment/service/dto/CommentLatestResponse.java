package com.zzang.chongdae.comment.service.dto;

import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import java.time.LocalDateTime;

public record CommentLatestResponse(String content, LocalDateTime createdAt) implements Comparable<CommentLatestResponse> {

    public CommentLatestResponse(CommentEntity comment) {
        this(comment.getContent(), comment.getCreatedAt());
    }

    @Override
    public int compareTo(CommentLatestResponse other) {
        if (this.createdAt == null && other.createdAt == null) {
            return 0;
        }
        if (this.createdAt == null) {
            return 1;
        }
        if (other.createdAt == null) {
            return -1;
        }
        return other.createdAt.compareTo(this.createdAt);
    }
}
