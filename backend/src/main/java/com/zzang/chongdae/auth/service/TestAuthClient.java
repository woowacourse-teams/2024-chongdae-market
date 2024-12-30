package com.zzang.chongdae.auth.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestAuthClient implements AuthClient {

    @Override
    public String getUserInfo(String accessToken) {
        return accessToken;
    }
}
