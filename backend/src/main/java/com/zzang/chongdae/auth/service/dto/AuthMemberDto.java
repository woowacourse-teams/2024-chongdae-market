package com.zzang.chongdae.auth.service.dto;

import com.zzang.chongdae.member.repository.entity.MemberEntity;

public record AuthMemberDto(Long id, String nickname) {

    public AuthMemberDto(MemberEntity member) {
        this(member.getId(), member.getNickname());
    }
}
