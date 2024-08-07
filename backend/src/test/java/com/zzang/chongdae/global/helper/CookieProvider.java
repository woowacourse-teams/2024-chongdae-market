package com.zzang.chongdae.global.helper;

import com.zzang.chongdae.auth.service.dto.LoginRequest;
import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;


@Component
public class CookieProvider {

    public Cookies createCookies() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginRequest("dora1234"))
                .when().post("/auth/login")
                .then().log().all()
                .extract().detailedCookies();
    }

    public Cookies createCookiesWithCi(String ci) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginRequest(ci))
                .when().post("/auth/login")
                .then().log().all()
                .extract().detailedCookies();
    }
}
