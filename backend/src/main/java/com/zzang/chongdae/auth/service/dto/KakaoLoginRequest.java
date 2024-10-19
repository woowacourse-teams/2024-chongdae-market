package com.zzang.chongdae.auth.service.dto;

import javax.annotation.Nullable;

public record KakaoLoginRequest(String accessToken,

                                @Nullable
                                String fcmToken) {
}
