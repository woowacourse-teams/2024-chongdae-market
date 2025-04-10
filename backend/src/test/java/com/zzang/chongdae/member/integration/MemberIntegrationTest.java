package com.zzang.chongdae.member.integration;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.zzang.chongdae.global.helper.ConcurrencyExecutor;
import com.zzang.chongdae.global.integration.IntegrationTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.member.service.dto.NicknameRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;

public class MemberIntegrationTest extends IntegrationTest {

    @Autowired
    ConcurrencyExecutor concurrencyExecutor;

    @DisplayName("닉네임 변경")
    @Nested
    class ChangeNickname {

        List<FieldDescriptor> requestDescriptors = List.of(
                fieldWithPath("nickname").description("닉네임 (필수)")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("닉네임 변경")
                .description("닉네임을 변경합니다.")
                .requestFields(requestDescriptors)
                .requestSchema(schema("NicknameRequest"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .responseFields(failResponseDescriptors)
                .responseSchema(schema("NicknameFailResponse"))
                .build();

        MemberEntity member;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("poke");
        }

        @DisplayName("닉네임 변경에 성공한다.")
        @Test
        void should_changeNicknameSuccess_when_givenNicknameRequest() {
            NicknameRequest request = new NicknameRequest("pokidoki");

            RestAssured.given(spec).log().all()
                    .filter(document("change-nickname-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().patch("/member")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("요청 값에 빈값이 들어오는 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_emptyValue() {
            NicknameRequest request = new NicknameRequest("");

            RestAssured.given(spec).log().all()
                    .filter(document("chnage-nickname-fail-request-with-empty", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().patch("/member")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("닉네임의 허용 길이가 넘을 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_tooLongNickname() {
            NicknameRequest request = new NicknameRequest("12345678901");

            RestAssured.given(spec).log().all()
                    .filter(document("chnage-nickname-fail-request-with-too-long-nickname", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().patch("/member")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("이미 존재하는 닉네임으로 변경할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_nicknameAlreadyExist() {
            NicknameRequest request = new NicknameRequest("poke");

            RestAssured.given(spec).log().all()
                    .filter(document("chnage-nickname-fail-request-with-exist-nickname",
                            resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().patch("/member")
                    .then().log().all()
                    .statusCode(409);
        }

        @DisplayName("다른 사용자가 같은 닉네임으로 동시에 변경할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_differentMemberAndSameNickname() throws InterruptedException {
            MemberEntity poki = memberFixture.createMember("poki");
            MemberEntity doki = memberFixture.createMember("doki");

            NicknameRequest request = new NicknameRequest("pokidoki");
            List<Integer> statusCodes = concurrencyExecutor.execute(
                    () -> RestAssured.given().log().all()
                            .cookies(cookieProvider.createCookiesWithMember(poki))
                            .contentType(ContentType.JSON)
                            .body(request)
                            .when().patch("/member")
                            .statusCode(),
                    () -> RestAssured.given().log().all()
                            .cookies(cookieProvider.createCookiesWithMember(doki))
                            .contentType(ContentType.JSON)
                            .body(request)
                            .when().patch("/member")
                            .statusCode());
            assertThat(statusCodes).containsExactlyInAnyOrder(200, 409);
        }
    }
}
