package com.zzang.chongdae.comment.service.dto;

public record CommentSaveRequest(Long memberId, Long offeringId, String content) {
}
