package com.zzang.chongdae.offeringmember.integration;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.zzang.chongdae.global.integration.IntegrationTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.service.dto.ParticipationRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;

public class OfferingMemberIntegrationTest extends IntegrationTest {

    @DisplayName("공모 참여")
    @Nested
    class Participate {

        List<FieldDescriptor> requestDescriptors = List.of(
                fieldWithPath("offeringId").description("공모 id (필수)")
        );
        ResourceSnippetParameters successSnippets = ResourceSnippetParameters.builder()
                .summary("공모 참여")
                .description("게시된 공모에 참여합니다.")
                .requestFields(requestDescriptors)
                .requestSchema(schema("ParticipationSuccessRequest"))
                .build();
        ResourceSnippetParameters failSnippets = ResourceSnippetParameters.builder()
                .summary("공모 참여")
                .description("게시된 공모에 참여합니다.")
                .requestFields(requestDescriptors)
                .responseFields(failResponseDescriptors)
                .requestSchema(schema("ParticipationFailRequest"))
                .responseSchema(schema("ParticipationSuccessResponse"))
                .build();
        MemberEntity proposer;
        MemberEntity participant;
        OfferingEntity offering;

        @BeforeEach
        void setUp() {
            proposer = memberFixture.createMember();
            participant = memberFixture.createMember("poke");
            offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);
        }

        @DisplayName("게시된 공모에 참여할 수 있다")
        @Test
        void should_participateSuccess() {
            ParticipationRequest request = new ParticipationRequest(
                    offering.getId()
            );
            RestAssured.given(spec).log().all()
                    .filter(document("participate-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithCi("poke5678"))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/participations")
                    .then().log().all()
                    .statusCode(201);
        }

        @DisplayName("공모자는 본인이 만든 공모에 참여할 수 없다")
        @Test
        void should_throwException_when_givenProposerParticipate() {
            ParticipationRequest request = new ParticipationRequest(
                    offering.getId()
            );
            RestAssured.given(spec).log().all()
                    .filter(document("participate-fail-my-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookies())
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/participations")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("유효하지 않은 공모에 참여할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidOffering() {
            ParticipationRequest request = new ParticipationRequest(
                    offering.getId() + 100
            );
            RestAssured.given(spec).log().all()
                    .filter(document("participate-fail-invalid-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithCi("poke5678"))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/participations")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("요청 값에 빈값이 들어오는 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_emptyValue() {
            ParticipationRequest request = new ParticipationRequest(
                    null
            );
            RestAssured.given(spec).log().all()
                    .filter(document("participate-fail-request-with-null", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithCi("poke5678"))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/participations")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("공모 참여자 목록 조회")
    @Nested
    class OfferingParticipants {
        List<ParameterDescriptorWithType> queryParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id (필수)")
        );
        List<FieldDescriptor> successResponseDescriptors = List.of(
                fieldWithPath("proposer.nickname").description("공모 작성자 닉네임"),
                fieldWithPath("participants[].nickname").description("공모 참여자 닉네임")
        );
        ResourceSnippetParameters successSnippets = ResourceSnippetParameters.builder()
                .summary("공모 참여자 목록 조회")
                .description("공모 참여자들의 목록을 조회합니다.")
                .queryParameters(queryParameterDescriptors)
                .responseFields(successResponseDescriptors)
                .requestSchema(schema("OfferingParticipantsSuccessRequest"))
                .responseSchema(schema("OfferingParticipantsSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = ResourceSnippetParameters.builder()
                .summary("공모 참여자 목록 조회")
                .description("공모 참여자들의 목록을 조회합니다.")
                .queryParameters(queryParameterDescriptors)
                .responseFields(failResponseDescriptors)
                .requestSchema(schema("OfferingParticipantsFailRequest"))
                .responseSchema(schema("OfferingParticipantsFailResponse"))
                .build();
        MemberEntity proposer;
        MemberEntity participant;
        OfferingEntity offering;

        @BeforeEach
        void setUp() {
            proposer = memberFixture.createMember("dora");
            participant = memberFixture.createMember("poke");
            offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);
            offeringMemberFixture.createParticipant(participant, offering);
        }

        @DisplayName("게시된 공모의 참여자 목록을 확인할 수 있다")
        @Test
        void should_participantsSuccess() {
            RestAssured.given(spec).log().all()
                    .filter(document("participants-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithCi("dora5678"))
                    .queryParam("offering-id", offering.getId())
                    .when().get("/participants")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("유효하지 않은 공모의 참여자 목록을 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidOffering() {
            RestAssured.given(spec).log().all()
                    .filter(document("participants-fail-invalid-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithCi("dora5678"))
                    .queryParam("offering-id", 1000)
                    .when().get("/participants")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("요청 값에 빈값이 들어오는 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_emptyValue() {
            RestAssured.given(spec).log().all()
                    .filter(document("participants-fail-request-with-null", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithCi("dora5678"))
                    .when().get("/participants?offering-id=")
                    .then().log().all()
                    .statusCode(400);
        }
    }
}
