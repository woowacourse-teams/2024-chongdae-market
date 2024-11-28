package com.zzang.chongdae.global.helper;

import com.zzang.chongdae.auth.service.dto.LoginRequest;
import com.zzang.chongdae.auth.service.dto.TokenDto;
import io.restassured.RestAssured;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class TestTokenProvider {

    public TokenDto createTokens() { // TODO: 로그인 API 호출 대신 토큰 생성 로직으로 대체하기
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginRequest("dora1234"))
                .when().post("/auth/login")
                .then().log().all()
                .extract().response().as(TokenDto.class);
    }

    public TokenDto createTokensWithCi(String ci) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginRequest(ci))
                .when().post("/auth/login")
                .then().log().all()
                .extract().response().as(TokenDto.class);
    }
}
