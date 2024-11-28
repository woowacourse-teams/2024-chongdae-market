package com.zzang.chongdae.comment.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentSaveRequest(@NotNull
                                 Long memberId,

                                 @NotNull
                                 Long offeringId,

                                 @NotBlank(message = "댓글 내용을 입력해주세요.")
                                 @Size(max = 80, message = "댓글 내용의 길이를 확인해주세요.")
                                 String content) {
}
