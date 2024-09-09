package com.zzang.chongdae.auth.integration;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.epages.restdocs.apispec.Schema.schema;
import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import com.epages.restdocs.apispec.HeaderDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.zzang.chongdae.auth.service.AuthClient;
import com.zzang.chongdae.auth.service.dto.KakaoLoginRequest;
import com.zzang.chongdae.global.integration.IntegrationTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.http.ContentType;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.FieldDescriptor;

class AuthIntegrationTest extends IntegrationTest {

    @MockBean
    AuthClient authClient;

    @DisplayName("카카오 로그인")
    @Nested
    class KakaoLogin {

        List<FieldDescriptor> requestDescriptors = List.of(
                fieldWithPath("accessToken").description("카카오 인증 토큰")
        );
        List<FieldDescriptor> responseDescriptors = List.of(
                fieldWithPath("memberId").description("회원 id"),
                fieldWithPath("nickname").description("닉네임")
        );
        List<HeaderDescriptorWithType> responseHeaderDescriptors = List.of(
                headerWithName("Set-Cookie").description("""
                        access_token=a.b.c; Path=/; HttpOnly \n
                        refresh_token=a.b.c; Path=/; HttpOnly
                        """)
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("카카오 로그인")
                .description("카카오 인증 토큰으로 로그인 합니다.")
                .requestFields(requestDescriptors)
                .responseFields(responseDescriptors)
                .responseHeaders(responseHeaderDescriptors)
                .requestSchema(schema("KakaoLonginRequest"))
                .responseSchema(schema("KakaoLoginResponse"))
                .build();

        MemberEntity member;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("dora");
            BDDMockito.given(authClient.getKakaoUserInfo(any()))
                    .willReturn(member.getLoginId());
        }

        @DisplayName("카카오 인증 토큰으로 로그인한다.")
        @Test
        void should_loginSuccess_when_givenMemberCI() {
            KakaoLoginRequest request = new KakaoLoginRequest(
                    "whatever"
            );

            given(spec).log().all()
                    .filter(document("kakao-login-success", resource(successSnippets)))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/auth/login/kakao")
                    .then().log().all()
                    .statusCode(200);
        }
    }

    @DisplayName("토큰 재발급")
    @Nested
    class Refresh {

        List<HeaderDescriptorWithType> responseHeaderDescriptors = List.of(
                headerWithName("Set-Cookie").description("""
                        access_token=a.b.c; Path=/; HttpOnly \n
                        refresh_token=a.b.c; Path=/; HttpOnly
                        """)
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("토큰 재발급")
                .description("토큰을 재발급합니다.")
                .responseHeaders(responseHeaderDescriptors)
                .build();
        ResourceSnippetParameters failedSnippets = builder()
                .responseFields(failResponseDescriptors)
                .requestSchema(schema("RefreshFailResponse"))
                .build();

        @Value("${security.jwt.token.refresh-secret-key}")
        String refreshSecretKey;

        @Value("${security.jwt.token.refresh-token-expired}")
        Duration refreshTokenExpired;

        MemberEntity member;
        Date now;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("dora");
            now = Date.from(clock.instant());
        }

        @DisplayName("refreshToken으로 accessToken과 refreshToken을 재발급 한다.")
        @Test
        void should_refreshSuccess_when_givenRefreshToken() {

            given(spec).log().all()
                    .filter(document("refresh-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .when().post("/auth/refresh")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("유효하지 않은 refeshToken인 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_givenInvalidRefreshToken() {

            given(spec).log().all()
                    .filter(document("refresh-fail-invalid-token", resource(failedSnippets)))
                    .cookie("refresh_token", "invalidRefreshToken")
                    .when().post("/auth/refresh")
                    .then().log().all()
                    .statusCode(401);
        }

        @DisplayName("만료된 refeshToken인 경우 예외 발생 후 403 코드를 반환한다.")
        @Test
        void should_throwException_when_givenExpiredRefreshToken() {
            Date alreadyExpiredAt = new Date(now.getTime() - refreshTokenExpired.toMillis());
            String expiredToken = Jwts.builder()
                    .setSubject(member.getId().toString())
                    .setExpiration(alreadyExpiredAt)
                    .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(refreshSecretKey.getBytes()))
                    .compact();

            given(spec).log().all()
                    .filter(document("refresh-fail-expired-token", resource(failedSnippets)))
                    .cookie("refresh_token", expiredToken)
                    .when().post("/auth/refresh")
                    .then().log().all()
                    .statusCode(403);
        }
    }
}
