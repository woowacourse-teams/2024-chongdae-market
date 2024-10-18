package com.zzang.chongdae.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginMember {

    private final Long id;
    private final String nickname;
}
