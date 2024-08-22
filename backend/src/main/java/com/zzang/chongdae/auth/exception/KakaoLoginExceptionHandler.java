package com.zzang.chongdae.auth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzang.chongdae.auth.service.dto.KakaoLoginFailResponseDto;
import com.zzang.chongdae.global.exception.MarketException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

@Slf4j
public class KakaoLoginExceptionHandler implements ResponseErrorHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        throw new MarketException(getKakaoLoginErrorCode(response));
    }

    private AuthErrorCode getKakaoLoginErrorCode(final ClientHttpResponse response) throws IOException {
        KakaoLoginFailResponseDto kakaoLoginFailResponse = objectMapper.readValue(
                response.getBody(), KakaoLoginFailResponseDto.class);
        log.error(kakaoLoginFailResponse.toString());
        return AuthErrorCode.KAKAO_LOGIN_INTERNAL_SERVER_ERROR;
    }
}
