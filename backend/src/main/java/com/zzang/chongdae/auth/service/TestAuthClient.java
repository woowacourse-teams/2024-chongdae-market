package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.global.exception.MarketException;
import org.springframework.web.client.RestClient;

public class TestAuthClient extends AuthClient {

    public TestAuthClient(RestClient restClient) {
        super(restClient);
    }

    @Override
    public String getKakaoUserInfo(String accessToken) {
        try {
            return super.getKakaoUserInfo(accessToken);
        } catch (MarketException e) {
            return accessToken;
        }
    }
}
