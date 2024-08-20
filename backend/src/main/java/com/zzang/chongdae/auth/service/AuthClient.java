package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.auth.service.dto.KakaoUserInfoResponseDto;
import com.zzang.chongdae.member.domain.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class AuthClient {

    private static final String BEARER_HEADER_FORMAT = "Bearer %s";
    private static final String GET_KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    private final RestClient restClient;

    public String getKakaoUserInfo(String accessToken) {
        KakaoUserInfoResponseDto responseDto = restClient.get()
                .uri(GET_KAKAO_USER_INFO_URI)
                .header(HttpHeaders.AUTHORIZATION, createAuthorization(accessToken))
                .retrieve()
                .body(KakaoUserInfoResponseDto.class);
        return AuthProvider.KAKAO.buildLoginId(responseDto.id()); // TODO: NPE 처리 고려하기
    }

    private String createAuthorization(String accessToken) {
        return BEARER_HEADER_FORMAT.formatted(accessToken);
    }
}
