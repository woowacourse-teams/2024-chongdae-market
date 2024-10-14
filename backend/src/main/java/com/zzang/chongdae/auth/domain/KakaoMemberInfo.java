package com.zzang.chongdae.auth.domain;

import com.zzang.chongdae.member.domain.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoMemberInfo {

    private final AuthProvider provider = AuthProvider.KAKAO;
    private final String loginId;

    public KakaoMemberInfo(Long kakaoId) {
        this.loginId = AuthProvider.KAKAO.buildLoginId(kakaoId.toString());
    }
}
