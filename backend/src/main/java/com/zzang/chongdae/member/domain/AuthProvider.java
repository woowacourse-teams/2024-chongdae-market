package com.zzang.chongdae.member.domain;

public enum AuthProvider {

    KAKAO;

    public String buildLoginId(Long loginId) {
        return this.name() + loginId;
    }
}
