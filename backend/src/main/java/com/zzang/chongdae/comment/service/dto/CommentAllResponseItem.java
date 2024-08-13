package com.zzang.chongdae.comment.service.dto;

import java.time.LocalDateTime;

public record CommentAllResponseItem(Long commentId,
                                     CommentCreatedAtResponse createdAt,
                                     String content,
                                     String nickname,
                                     Boolean isProposer,
                                     Boolean isMine) {

    public CommentAllResponseItem(Long commentId, LocalDateTime createdAt, String content,
                                  String nickname, Boolean isProposer, Boolean isMine) {
        this(commentId, new CommentCreatedAtResponse(createdAt), content, nickname, isProposer, isMine);
    }
}
