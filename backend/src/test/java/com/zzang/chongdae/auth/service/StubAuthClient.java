package com.zzang.chongdae.auth.service;

public class StubAuthClient implements AuthClient {

    @Override
    public String getUserInfo(String accessToken) {
        return "stubUserInfo";
    }
}
