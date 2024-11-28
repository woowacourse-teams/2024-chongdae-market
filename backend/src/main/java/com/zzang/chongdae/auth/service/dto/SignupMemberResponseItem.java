package com.zzang.chongdae.auth.service.dto;

import com.zzang.chongdae.member.repository.entity.MemberEntity;

public record SignupMemberResponseItem(Long id, String nickname) {

    public SignupMemberResponseItem(MemberEntity member) {
        this(member.getId(), member.getNickname());
    }
}
