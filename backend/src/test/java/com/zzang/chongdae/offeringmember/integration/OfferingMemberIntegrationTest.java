package com.zzang.chongdae.offeringmember.integration;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

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
                fieldWithPath("memberId").description("회원 id (필수)"),
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
            proposer = memberFixture.createMember("dora");
            participant = memberFixture.createMember("poke");
            offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);
        }

        @DisplayName("게시된 공모에 참여할 수 있다")
        @Test
        void should_participateSuccess() {
            ParticipationRequest request = new ParticipationRequest(
                    participant.getId(),
                    offering.getId()
            );
            RestAssured.given(spec).log().all()
                    .filter(document("participate-success", resource(successSnippets)))
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
                    proposer.getId(),
                    offering.getId()
            );
            RestAssured.given(spec).log().all()
                    .filter(document("participate-fail-my-offering", resource(failSnippets)))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/participations")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("유효하지 않은 사용자가 공모에 참여할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidMember() {
            ParticipationRequest request = new ParticipationRequest(
                    participant.getId() + 100,
                    offering.getId()
            );
            RestAssured.given(spec).log().all()
                    .filter(document("participate-fail-invalid-member", resource(failSnippets)))
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
                    participant.getId(),
                    offering.getId() + 100
            );
            RestAssured.given(spec).log().all()
                    .filter(document("participate-fail-invalid-offering", resource(failSnippets)))
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
                    null,
                    null
            );
            RestAssured.given(spec).log().all()
                    .filter(document("participate-fail-request-with-null", resource(failSnippets)))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/participations")
                    .then().log().all()
                    .statusCode(400);
        }
    }
}
