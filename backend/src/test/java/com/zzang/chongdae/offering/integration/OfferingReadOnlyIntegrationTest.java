package com.zzang.chongdae.offering.integration;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.Schema.schema;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.zzang.chongdae.global.integration.IntegrationTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.OfferingFilter;
import com.zzang.chongdae.offering.domain.OfferingFilterType;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;

public class OfferingReadOnlyIntegrationTest extends IntegrationTest {

    @DisplayName("공모 상세 조회(읽기 전용)")
    @Nested
    class ReadOnlyGetOffering {
        List<ParameterDescriptorWithType> pathParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id (필수)")
        );
        List<FieldDescriptor> successResponseDescriptors = List.of(
                fieldWithPath("id").description("공모 id"),
                fieldWithPath("title").description("제목"),
                fieldWithPath("meetingAddressDong").description("모집 동 주소"),
                fieldWithPath("currentCount").description("현재원"),
                fieldWithPath("totalCount").description("총원"),
                fieldWithPath("thumbnailUrl").description("사진 링크"),
                fieldWithPath("dividedPrice").description("n빵 가격"),
                fieldWithPath("originPrice").description("원 가격"),
                fieldWithPath("discountRate").description("할인율"),
                fieldWithPath("status").description("공모 상태"
                        + getEnumValuesAsString(OfferingStatus.class)),
                fieldWithPath("isOpen").description("공모 참여 가능 여부")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("공모 단건 조회")
                .description("공모 단건을 조회합니다.")
                .pathParameters(pathParameterDescriptors)
                .responseFields(successResponseDescriptors)
                .responseSchema(schema("OfferingReadOnlySuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .summary("공모 단건 조회")
                .description("공모 id를 통해 공모의 단건 정보를 조회합니다.")
                .pathParameters(pathParameterDescriptors)
                .responseFields(failResponseDescriptors)
                .responseSchema(schema("OfferingReadOnlyFailResponse"))
                .build();

        @BeforeEach
        void setUp() {
            MemberEntity proposer = memberFixture.createMember("dora");
            OfferingEntity offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);
        }

        @DisplayName("공모 단건을 조회할 수 있다")
        @Test
        void should_responseOffering_when_givenOfferingId() {
            given(spec).log().all()
                    .filter(document("get-offering-read-only-success", resource(successSnippets)))
                    .pathParam("offering-id", 1)
                    .when().get("/read-only/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("유효하지 않은 공모를 단건 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidOffering() {
            given(spec).log().all()
                    .filter(document("get-offering-read-only-fail-invalid-offering", resource(failSnippets)))
                    .pathParam("offering-id", 100)
                    .when().get("/read-only/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("공모 목록 조회(읽기 전용)")
    @Nested
    class ReadOnlyGetAllOffering {

        List<ParameterDescriptorWithType> queryParameterDescriptors = List.of(
                parameterWithName("filter").description("필터 이름 (기본값: RECENT)"
                        + getEnumValuesAsString(OfferingFilter.class)).optional(),
                parameterWithName("search").description("검색어").optional(),
                parameterWithName("last-id").description("마지막 공모 id").optional(),
                parameterWithName("page-size").description("페이지 크기 (기본값: 10)").optional()
        );
        List<FieldDescriptor> successResponseDescriptors = List.of(
                fieldWithPath("offerings[].id").description("공모 id"),
                fieldWithPath("offerings[].title").description("제목"),
                fieldWithPath("offerings[].meetingAddressDong").description("모집 동 주소"),
                fieldWithPath("offerings[].currentCount").description("현재원"),
                fieldWithPath("offerings[].totalCount").description("총원"),
                fieldWithPath("offerings[].thumbnailUrl").description("사진 링크"),
                fieldWithPath("offerings[].dividedPrice").description("n빵 가격"),
                fieldWithPath("offerings[].originPrice").description("원 가격"),
                fieldWithPath("offerings[].discountRate").description("할인율"),
                fieldWithPath("offerings[].status").description("공모 상태"
                        + getEnumValuesAsString(OfferingStatus.class)),
                fieldWithPath("offerings[].isOpen").description("공모 참여 가능 여부")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("공모 목록 조회")
                .description("공모 목록을 조회합니다.")
                .queryParameters(queryParameterDescriptors)
                .responseFields(successResponseDescriptors)
                .responseSchema(schema("OfferingAllReadOnlySuccessResponse"))
                .build();


        @BeforeEach
        void setUp() {
            MemberEntity proposer = memberFixture.createMember("dora");

            for (int i = 0; i < 11; i++) {
                OfferingEntity offering = offeringFixture.createOffering(proposer);
                offeringMemberFixture.createProposer(proposer, offering);
            }
        }

        @DisplayName("공모 목록을 조회할 수 있다")
        @Test
        void should_responseAllOffering_when_givenPageInfo() {
            given(spec).log().all()
                    .filter(document("get-all-offering-read-only-success", resource(successSnippets)))
                    .queryParam("filter", "RECENT")
                    .queryParam("search", "title")
                    .queryParam("last-id", 10)
                    .queryParam("page-size", 10)
                    .when().get("/read-only/offerings")
                    .then().log().all()
                    .statusCode(200);
        }
    }

    @DisplayName("공모 필터 목록 조회(읽기 전용)")
    @Nested
    class ReadOnlyGetAllOfferingFilter {

        List<FieldDescriptor> successResponseDescriptors = List.of(
                fieldWithPath("filters[].name").description("필터 이름"
                        + getEnumValuesAsString(OfferingFilter.class)),
                fieldWithPath("filters[].value").description("필터 디스플레이 이름"),
                fieldWithPath("filters[].type").description("필터 디스플레이 여부"
                        + getEnumValuesAsString(OfferingFilterType.class))
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("공모 필터 목록 조회")
                .description("공모 목록 조회 시 필터링할 수 있는 키워드 목록을 조회합니다.")
                .responseFields(successResponseDescriptors)
                .responseSchema(schema("OfferingFilterReadOnlySuccessResponse"))
                .build();


        @BeforeEach
        void setUp() {
            memberFixture.createMember("dora");
        }

        @DisplayName("공모 id로 공모 일정 정보를 조회할 수 있다")
        @Test
        void should_responseOfferingFilter_when_givenOfferingId() {
            given(spec).log().all()
                    .filter(document("get-all-offering-filter-read-only-success", resource(successSnippets)))
                    .when().get("/read-only/offerings/filters")
                    .then().log().all()
                    .statusCode(200);
        }
    }
}
