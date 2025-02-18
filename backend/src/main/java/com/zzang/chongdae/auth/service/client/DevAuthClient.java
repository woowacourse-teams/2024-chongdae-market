package com.zzang.chongdae.auth.service.client;

import com.zzang.chongdae.global.exception.MarketException;
import org.springframework.web.client.RestClient;

public class DevAuthClient extends ProdAuthClient {

    public DevAuthClient(RestClient restClient) {
        super(restClient);
    }

    @Override
    protected String getKakaoUserInfo(String accessToken) {
        try {
            return super.getKakaoUserInfo(accessToken);
        } catch (MarketException e) {
            return accessToken;
        }
    }
}
