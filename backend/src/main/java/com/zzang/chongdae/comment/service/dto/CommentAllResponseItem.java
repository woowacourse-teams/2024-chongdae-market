package com.zzang.chongdae.comment.service.dto;

public record CommentAllResponseItem(CommentCreatedAtResponse createdAt,
                                     String content,
                                     String nickname,
                                     Boolean isProposer,
                                     Boolean isMine) {
}
