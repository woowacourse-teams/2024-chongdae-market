package com.zzang.chongdae.analytic.integration;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.Schema.schema;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.zzang.chongdae.global.integration.IntegrationTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;

public class AnalyticIntegrationTest extends IntegrationTest {

    @DisplayName("AB 테스트")
    @Nested
    class AbTest {

        List<FieldDescriptor> successResponseDescriptors = List.of(
                fieldWithPath("type").description("AB 테스트 그룹")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("AB 테스트 그룹 조회")
                .description("현재 로그인 한 사용자의 AB 테스트 그룹을 조회합니다.")
                .responseFields(successResponseDescriptors)
                .responseSchema(schema("VariantSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .summary("AB 테스트 그룹 조회")
                .description("현재 로그인 한 사용자의 AB 테스트 그룹을 조회합니다.")
                .responseFields(failResponseDescriptors)
                .responseSchema(schema("VariantFailResponse"))
                .build();

        MemberEntity member;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("poke");
        }

        @DisplayName("로그인 한 사용자의 AB 테스트 그룹을 반환한다.")
        @Test
        void should_responseVariantType_when_getVariant() {
            given(spec).log().all()
                    .filter(document("get-variant-success", resource(successSnippets)))
                    .contentType(ContentType.JSON)
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .when().get("/analytics/variant")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("로그인 하지 않을 경우 AB 테스트 그룹을 반환하지 않는다.")
        @Test
        void should_throwException_when_getVariantWithoutLogin() {
            given(spec).log().all()
                    .filter(document("get-variant-fail", resource(failSnippets)))
                    .contentType(ContentType.JSON)
                    .when().get("/analytics/variant")
                    .then().log().all()
                    .statusCode(401);
        }
    }
}
