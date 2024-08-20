package com.zzang.chongdae.member.domain;

public enum AuthProvider {

    KAKAO;

    public String buildLoginId(String loginId) {
        return this.name() + loginId;
    }
}
