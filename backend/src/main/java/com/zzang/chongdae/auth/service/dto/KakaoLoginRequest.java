package com.zzang.chongdae.auth.service.dto;

import jakarta.validation.constraints.NotEmpty;

public record KakaoLoginRequest(String accessToken,

                                @NotEmpty
                                String fcmToken) {
}
