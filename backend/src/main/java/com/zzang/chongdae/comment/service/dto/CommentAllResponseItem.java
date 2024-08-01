package com.zzang.chongdae.comment.service.dto;

public record CommentAllResponseItem(Long commentId,
                                     CommentCreatedAtResponse createdAt,
                                     String content,
                                     String nickname,
                                     Boolean isProposer,
                                     Boolean isMine) {
}
