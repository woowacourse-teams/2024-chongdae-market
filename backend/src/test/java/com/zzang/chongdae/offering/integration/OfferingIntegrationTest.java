package com.zzang.chongdae.offering.integration;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.zzang.chongdae.global.integration.IntegrationTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;

public class OfferingIntegrationTest extends IntegrationTest {

    @DisplayName("공모 상세 조회")
    @Nested
    class GetOfferingDetail {

        List<ParameterDescriptorWithType> offeringDetailPathParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id")
        );
        List<ParameterDescriptorWithType> offeringDetailQueryParameterDescriptors = List.of(
                parameterWithName("member-id").description("회원 id")
        );
        List<FieldDescriptor> offeringDetailResponseDescriptors = List.of(
                fieldWithPath("id").description("공모 id"),
                fieldWithPath("title").description("제목"),
                fieldWithPath("productUrl").description("물품 링크"),
                fieldWithPath("meetingAddress").description("모집 주소"),
                fieldWithPath("meetingAddressDetail").description("모집 상세 주소"),
                fieldWithPath("description").description("내용"),
                fieldWithPath("deadline").description("마감시간"),
                fieldWithPath("currentCount").description("현재원"),
                fieldWithPath("totalCount").description("총원"),
                fieldWithPath("thumbnailUrl").description("사진 링크"),
                fieldWithPath("dividedPrice").description("n빵 가격"),
                fieldWithPath("totalPrice").description("총가격"),
                fieldWithPath("condition").description("공모 상태"),
                fieldWithPath("memberId").description("공모자 회원 id"),
                fieldWithPath("nickname").description("공모자 회원 닉네임"),
                fieldWithPath("isParticipated").description("공모 참여 여부")
        );
        ResourceSnippetParameters snippets = builder()
                .summary("공모 상세 조회")
                .description("공모 id를 통해 공모의 상세 정보를 조회합니다.")
                .pathParameters(offeringDetailPathParameterDescriptors)
                .queryParameters(offeringDetailQueryParameterDescriptors)
                .responseFields(offeringDetailResponseDescriptors)
                .responseSchema(schema("OfferingDetailResponse"))
                .build();

        @BeforeEach
        void setUp() {
            MemberEntity member = memberFixture.createMember();
            offeringFixture.createOffering(member);
        }

        @DisplayName("공모 id로 공모 상세 정보를 조회할 수 있다")
        @Test
        void should_responseOfferingDetail_when_givenOfferingId() {
            RestAssured.given(spec).log().all()
                    .filter(document("get-offering-detail-success", resource(snippets)))
                    .pathParam("offering-id", 1)
                    .queryParam("member-id", 1)
                    .when().get("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(200);
        }
    }

    @DisplayName("공모 목록 조회")
    @Nested
    class GetAllOffering {

        List<ParameterDescriptorWithType> offeringAllQueryParameterDescriptors = List.of(
                parameterWithName("last-id").description("마지막 공모 id"),
                parameterWithName("page-size").description("페이지 크기")
        );
        List<FieldDescriptor> offeringAllResponseDescriptors = List.of(
                fieldWithPath("offerings[].id").description("공모 id"),
                fieldWithPath("offerings[].title").description("제목"),
                fieldWithPath("offerings[].meetingAddressDong").description("모집 동 주소"),
                fieldWithPath("offerings[].currentCount").description("현재원"),
                fieldWithPath("offerings[].totalCount").description("총원"),
                fieldWithPath("offerings[].thumbnailUrl").description("사진 링크"),
                fieldWithPath("offerings[].dividedPrice").description("n빵 가격"),
                fieldWithPath("offerings[].condition").description("공모 상태"),
                fieldWithPath("offerings[].isOpen").description("공모 참여 가능 여부")
        );
        ResourceSnippetParameters snippets = builder()
                .summary("공모 목록 조회")
                .description("공모 목록을 조회합니다.")
                .queryParameters(offeringAllQueryParameterDescriptors)
                .responseFields(offeringAllResponseDescriptors)
                .responseSchema(schema("OfferingAllResponse"))
                .build();

        @BeforeEach
        void setUp() {
            MemberEntity member = memberFixture.createMember();
            for (int i = 0; i < 11; i++) {
                offeringFixture.createOffering(member);
            }
        }

        @DisplayName("공모 목록을 조회할 수 있다")
        @Test
        void should_responseAllOffering_when_givenPageInfo() {
            RestAssured.given(spec).log().all()
                    .filter(document("get-all-offering-success", resource(snippets)))
                    .queryParam("last-id", 1)
                    .queryParam("page-size", 10)
                    .when().get("/offerings")
                    .then().log().all()
                    .statusCode(200);
        }
    }

    @DisplayName("공모 일정 조회")
    @Nested
    class GetOfferingMeeting {

        List<ParameterDescriptorWithType> offeringMeetingPathParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id")
        );
        List<FieldDescriptor> offeringMeetingResponseDescriptors = List.of(
                fieldWithPath("deadline").description("마감시간"),
                fieldWithPath("meetingAddress").description("모집 주소"),
                fieldWithPath("meetingAddressDetail").description("모집 상세 주소")
        );
        ResourceSnippetParameters snippets = builder()
                .summary("공모 일정 조회")
                .description("공모 id를 통해 공모의 일정 정보를 조회합니다.")
                .pathParameters(offeringMeetingPathParameterDescriptors)
                .responseFields(offeringMeetingResponseDescriptors)
                .responseSchema(schema("OfferingMeetingResponse"))
                .build();

        @BeforeEach
        void setUp() {
            MemberEntity member = memberFixture.createMember();
            offeringFixture.createOffering(member);
        }

        @DisplayName("공모 id로 공모 일정 정보를 조회할 수 있다")
        @Test
        void should_responseOfferingMeeting_when_givenOfferingId() {
            RestAssured.given(spec).log().all()
                    .filter(document("get-offering-meeting-success", resource(snippets)))
                    .pathParam("offering-id", 1)
                    .when().get("/offerings/{offering-id}/meetings")
                    .then().log().all()
                    .statusCode(200);
        }
    }

    @DisplayName("공모 작성")
    @Nested
    class CreateOffering {

        List<FieldDescriptor> offeringCreateRequestDescriptors = List.of(
                fieldWithPath("memberId").description("회원 id"),
                fieldWithPath("title").description("제목"),
                fieldWithPath("productUrl").description("물품 구매 링크"),
                fieldWithPath("thumbnailUrl").description("사진 링크"),
                fieldWithPath("totalCount").description("총원"),
                fieldWithPath("totalPrice").description("총가격"),
                fieldWithPath("eachPrice").description("낱개 가격"),
                fieldWithPath("meetingAddress").description("모집 주소"),
                fieldWithPath("meetingAddressDetail").description("모집 상세 주소"),
                fieldWithPath("meetingAddressDong").description("모집 동 주소"),
                fieldWithPath("deadline").description("모집 종료 시간"),
                fieldWithPath("description").description("내용")
        );
        ResourceSnippetParameters snippets = builder()
                .summary("공모 작성")
                .description("공모 정보를 받아 공모를 작성합니다.")
                .requestFields(offeringCreateRequestDescriptors)
                .responseSchema(schema("OfferingCreateResponse"))
                .build();
        MemberEntity member;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember();
        }

        @DisplayName("공모 정보를 받아 공모를 작성합니다")
        @Test
        void should_createOffering_when_givenOfferingCreateRequest() {
            Map<String, String> request = new HashMap<>(Map.of(
                    "memberId", member.getId().toString(),
                    "title", "공모 제목",
                    "productUrl", "www.naver.com",
                    "thumbnailUrl", "www.naver.com/favicon.ico",
                    "totalCount", "5",
                    "totalPrice", "10000",
                    "eachPrice", "2000",
                    "meetingAddress", "서울특별시 광진구 구의강변로 3길 11",
                    "meetingAddressDetail", "상세주소아파트",
                    "meetingAddressDong", "구의동"));
            request.put("deadline", "2024-10-11T10:00:00");
            request.put("description", "내용입니다.");

            RestAssured.given(spec).log().all()
                    .filter(document("create-offering-success", resource(snippets)))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/offerings")
                    .then().log().all()
                    .statusCode(201);
        }
    }
}
