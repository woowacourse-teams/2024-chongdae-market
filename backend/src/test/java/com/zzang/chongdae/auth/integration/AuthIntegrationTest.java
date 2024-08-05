package com.zzang.chongdae.auth.integration;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.epages.restdocs.apispec.Schema.schema;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import com.epages.restdocs.apispec.HeaderDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.zzang.chongdae.auth.service.dto.LoginRequest;
import com.zzang.chongdae.global.integration.IntegrationTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;

class AuthIntegrationTest extends IntegrationTest {

    @DisplayName("로그인")
    @Nested
    class Login {

        List<FieldDescriptor> requestDescriptors = List.of(
                fieldWithPath("ci").description("회원 식별자 인증 정보")
        );
        List<HeaderDescriptorWithType> responseHeaderDescriptors = List.of(
                headerWithName("Set-Cookie").description("""
                        access_token=a.b.c; Path=/; HttpOnly \n
                        refresh_token=a.b.c; Path=/; HttpOnly
                        """)
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("회원 로그인")
                .description("회원 식별자 인증 정보로 로그인합니다.")
                .requestFields(requestDescriptors)
                .requestSchema(schema("LonginRequest"))
                .responseHeaders(responseHeaderDescriptors)
                .build();

        MemberEntity member;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember();
        }

        @DisplayName("회원 식별자 인증 정보로 로그인합니다.")
        @Test
        void should_loginSuccess_when_givenMemberCI() {
            LoginRequest request = new LoginRequest(
                    "dora1234"
            );

            given(spec).log().all()
                    .filter(document("login-success", resource(successSnippets)))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/auth/login")
                    .then().log().all()
                    .statusCode(200);
        }
    }
}
