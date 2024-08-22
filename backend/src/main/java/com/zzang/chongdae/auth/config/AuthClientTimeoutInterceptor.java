package com.zzang.chongdae.auth.config;

import com.zzang.chongdae.auth.exception.AuthErrorCode;
import com.zzang.chongdae.global.exception.MarketException;
import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class AuthClientTimeoutInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
        try {
            return execution.execute(request, body);
        } catch (IOException exception) {
            throw new MarketException(AuthErrorCode.CLIENT_TIME_OUT);
        }
    }
}
