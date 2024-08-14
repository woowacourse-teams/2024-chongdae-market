package com.zzang.chongdae.comment.service.dto;

import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.member.repository.entity.MemberEntity;

public record CommentAllResponseItem(Long commentId,
                                     CommentCreatedAtResponse createdAt,
                                     String content,
                                     String nickname,
                                     boolean isProposer,
                                     boolean isMine) {

    public CommentAllResponseItem(CommentEntity comment, MemberEntity member) {
        this(comment.getId(),
                new CommentCreatedAtResponse(comment.getCreatedAt()),
                comment.getContent(),
                comment.getMember().getNickname(),
                comment.getOffering().isProposedBy(comment.getMember()),
                comment.isOwnedBy(member));
    }
}
