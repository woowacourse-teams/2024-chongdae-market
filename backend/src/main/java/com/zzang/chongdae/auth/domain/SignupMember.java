package com.zzang.chongdae.auth.domain;

import com.zzang.chongdae.member.domain.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupMember {

    private final String nickname;
    private final AuthProvider provider;
    private final String loginId;
    private final String password;

    public SignupMember(String nickname, String password, KakaoMemberInfo info) {
        this(nickname, info.getProvider(), info.getLoginId(), password);
    }
}
