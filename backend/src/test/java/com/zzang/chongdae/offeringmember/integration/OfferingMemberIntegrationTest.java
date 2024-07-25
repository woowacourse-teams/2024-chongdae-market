package com.zzang.chongdae.offeringmember.integration;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.zzang.chongdae.global.integration.IntegrationTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;

public class OfferingMemberIntegrationTest extends IntegrationTest {

    @DisplayName("공모 참여")
    @Nested
    class Participate {

        List<FieldDescriptor> participationRequestDescriptors = List.of(
                fieldWithPath("memberId").description("회원 id"),
                fieldWithPath("offeringId").description("공모 id")
        );
        ResourceSnippetParameters snippets = ResourceSnippetParameters.builder()
                .summary("공모 참여")
                .description("게시된 공모에 참여합니다.")
                .requestFields(participationRequestDescriptors)
                .requestSchema(schema("ParticipationRequest"))
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
            Map<String, String> request = Map.of(
                    "memberId", participant.getId().toString(),
                    "offeringId", offering.getId().toString()
            );

            RestAssured.given(spec).log().all()
                    .filter(document("participate-success", resource(snippets)))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/participations")
                    .then().log().all()
                    .statusCode(201);
        }

        @DisplayName("공모자는 본인이 만든 공모에 참여할 수 없다")
        @Test
        void should_throwException_when_givenProposerParticipate() {
            Map<String, String> request = Map.of(
                    "memberId", proposer.getId().toString(),
                    "offeringId", offering.getId().toString()
            );

            RestAssured.given(spec).log().all()
                    .filter(document("participate-fail", resource(snippets)))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/participations")
                    .then().log().all()
                    .statusCode(400);
        }
    }
}
