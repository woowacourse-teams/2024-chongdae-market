package com.zzang.chongdae.notification.domain;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import lombok.Getter;

@Getter
public class FcmToken {

    private final String value;

    public FcmToken(MemberEntity member) {
        this.value = member.getFcmToken();
    }
}
