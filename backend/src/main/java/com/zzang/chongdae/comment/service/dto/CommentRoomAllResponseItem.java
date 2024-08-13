package com.zzang.chongdae.comment.service.dto;

public record CommentRoomAllResponseItem(Long offeringId,
                                         String offeringTitle,
                                         Boolean isProposer,
                                         CommentLatestResponse latestComment) {
}
