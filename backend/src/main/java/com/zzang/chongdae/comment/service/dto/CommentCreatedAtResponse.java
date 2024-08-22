package com.zzang.chongdae.comment.service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record CommentCreatedAtResponse(LocalDate date, LocalTime time) {

    public CommentCreatedAtResponse(LocalDateTime createdAt) {
        this(createdAt.toLocalDate(), createdAt.toLocalTime());
    }
}
