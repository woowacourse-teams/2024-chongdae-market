package com.zzang.chongdae.auth.service.dto;

import com.zzang.chongdae.auth.domain.AuthToken;
import com.zzang.chongdae.auth.domain.LoginMember;

public record AuthInfoDto(LoginMember loginMember, AuthToken authToken) {
}
