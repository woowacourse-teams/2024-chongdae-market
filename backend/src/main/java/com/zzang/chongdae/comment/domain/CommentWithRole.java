package com.zzang.chongdae.comment.domain;

import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.offeringmember.domain.OfferingMemberRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentWithRole {

    private final CommentEntity comment;
    private final OfferingMemberRole role;
}
