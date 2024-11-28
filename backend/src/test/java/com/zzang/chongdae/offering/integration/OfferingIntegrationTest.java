package com.zzang.chongdae.offering.integration;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.Schema.schema;
import static io.restassured.RestAssured.given;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.zzang.chongdae.global.integration.IntegrationTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.domain.OfferingFilter;
import com.zzang.chongdae.offering.domain.OfferingFilterType;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offering.service.dto.OfferingMeetingUpdateRequest;
import com.zzang.chongdae.offering.service.dto.OfferingProductImageRequest;
import com.zzang.chongdae.offering.service.dto.OfferingSaveRequest;
import com.zzang.chongdae.offering.service.dto.OfferingUpdateRequest;
import com.zzang.chongdae.storage.service.StorageService;
import io.restassured.http.ContentType;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.web.multipart.MultipartFile;

public class OfferingIntegrationTest extends IntegrationTest {

    @MockBean
    private StorageService storageService;

    @DisplayName("공모 상세 조회")
    @Nested
    class GetOfferingDetail {

        List<ParameterDescriptorWithType> pathParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id (필수)")
        );
        List<FieldDescriptor> successResponseDescriptors = List.of(
                fieldWithPath("id").description("공모 id (필수)"),
                fieldWithPath("title").description("제목 (필수)"),
                fieldWithPath("productUrl").description("물품 링크"),
                fieldWithPath("meetingAddress").description("모집 주소 (필수)"),
                fieldWithPath("meetingAddressDetail").description("모집 상세 주소"),
                fieldWithPath("description").description("내용 (필수)"),
                fieldWithPath("meetingDate").description("마감시간 (필수)"),
                fieldWithPath("currentCount").description("현재원 (필수)"),
                fieldWithPath("totalCount").description("총원 (필수)"),
                fieldWithPath("thumbnailUrl").description("사진 링크"),
                fieldWithPath("dividedPrice").description("n빵 가격 (필수)"),
                fieldWithPath("totalPrice").description("총가격 (필수)"),
                fieldWithPath("originPrice").description("원 가격"),
                fieldWithPath("status").description("공모 상태 (필수)"
                        + getEnumValuesAsString(OfferingStatus.class)),
                fieldWithPath("memberId").description("공모자 회원 id (필수)"),
                fieldWithPath("nickname").description("공모자 회원 닉네임 (필수)"),
                fieldWithPath("isProposer").description("공모자 여부 (필수)"),
                fieldWithPath("isParticipated").description("공모 참여 여부 (필수)")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("공모 상세 조회")
                .description("공모 id를 통해 공모의 상세 정보를 조회합니다.")
                .pathParameters(pathParameterDescriptors)
                .responseFields(successResponseDescriptors)
                .responseSchema(schema("OfferingDetailSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .summary("공모 상세 조회")
                .description("공모 id를 통해 공모의 상세 정보를 조회합니다.")
                .pathParameters(pathParameterDescriptors)
                .responseFields(failResponseDescriptors)
                .responseSchema(schema("OfferingDetailFailResponse"))
                .build();

        MemberEntity proposer;

        @BeforeEach
        void setUp() {
            proposer = memberFixture.createMember("dora");
            OfferingEntity offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);
        }

        @DisplayName("공모 id로 공모 상세 정보를 조회할 수 있다")
        @Test
        void should_responseOfferingDetail_when_givenOfferingId() {
            given(spec).log().all()
                    .filter(document("get-offering-detail-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .pathParam("offering-id", 1)
                    .when().get("/offerings/{offering-id}/detail")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("유효하지 않은 공모를 상세 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidOffering() {
            given(spec).log().all()
                    .filter(document("get-offering-detail-fail-invalid-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .pathParam("offering-id", 100)
                    .when().get("/offerings/{offering-id}/detail")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("공모 단건 조회")
    @Nested
    class GetOffering {

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
                .responseSchema(schema("OfferingSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .summary("공모 단건 조회")
                .description("공모 id를 통해 공모의 단건 정보를 조회합니다.")
                .pathParameters(pathParameterDescriptors)
                .responseFields(failResponseDescriptors)
                .responseSchema(schema("OfferingFailResponse"))
                .build();

        MemberEntity proposer;

        @BeforeEach
        void setUp() {
            proposer = memberFixture.createMember("dora");
            OfferingEntity offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);
        }

        @DisplayName("공모 단건을 조회할 수 있다")
        @Test
        void should_responseOffering_when_givenOfferingId() {
            given(spec).log().all()
                    .filter(document("get-offering-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .pathParam("offering-id", 1)
                    .when().get("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("유효하지 않은 공모를 단건 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidOffering() {
            given(spec).log().all()
                    .filter(document("get-offering-fail-invalid-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .pathParam("offering-id", 100)
                    .when().get("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("공모 목록 조회")
    @Nested
    class GetAllOffering {

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
                .responseSchema(schema("OfferingAllSuccessResponse"))
                .build();

        MemberEntity member;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("dora");
            for (int i = 0; i < 11; i++) {
                offeringFixture.createOffering(member);
            }
        }

        @DisplayName("공모 목록을 조회할 수 있다")
        @Test
        void should_responseAllOffering_when_givenPageInfo() {
            given(spec).log().all()
                    .filter(document("get-all-offering-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .queryParam("filter", "RECENT")
                    .queryParam("search", "title")
                    .queryParam("last-id", 10)
                    .queryParam("page-size", 10)
                    .when().get("/offerings")
                    .then().log().all()
                    .statusCode(200);
        }
    }

    @DisplayName("공모 일정 조회")
    @Nested
    class GetOfferingMeeting {

        List<ParameterDescriptorWithType> pathParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id (필수)")
        );
        List<FieldDescriptor> successResponseDescriptors = List.of(
                fieldWithPath("meetingDate").description("마감시간"),
                fieldWithPath("meetingAddress").description("모집 주소"),
                fieldWithPath("meetingAddressDetail").description("모집 상세 주소"),
                fieldWithPath("meetingAddressDong").description("모집 동 주소")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("공모 일정 조회")
                .description("공모 id를 통해 공모의 일정 정보를 조회합니다.")
                .pathParameters(pathParameterDescriptors)
                .responseFields(successResponseDescriptors)
                .responseSchema(schema("OfferingMeetingSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .summary("공모 일정 조회")
                .description("공모 id를 통해 공모의 일정 정보를 조회합니다.")
                .pathParameters(pathParameterDescriptors)
                .responseFields(failResponseDescriptors)
                .responseSchema(schema("OfferingMeetingFailResponse"))
                .build();

        MemberEntity member;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("dora");
            OfferingEntity offering = offeringFixture.createOffering(member);
            offeringMemberFixture.createProposer(member, offering);
        }

        @DisplayName("공모 id로 공모 일정 정보를 조회할 수 있다")
        @Test
        void should_responseOfferingMeeting_when_givenOfferingId() {
            given(spec).log().all()
                    .filter(document("get-offering-meeting-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .pathParam("offering-id", 1)
                    .when().get("/offerings/{offering-id}/meetings")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("유효하지 않은 공모의 일정 정보를 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidOffering() {
            given(spec).log().all()
                    .filter(document("get-offering-meeting-fail-invalid-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .pathParam("offering-id", 100)
                    .when().get("/offerings/{offering-id}/meetings")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("공모 일정 수정")
    @Nested
    class UpdateOfferingMeeting {

        List<ParameterDescriptorWithType> pathParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id (필수)")
        );
        List<FieldDescriptor> requestDescriptors = List.of(
                fieldWithPath("meetingDate").description("마감시간 (필수)"),
                fieldWithPath("meetingAddress").description("모집 주소 (필수)"),
                fieldWithPath("meetingAddressDetail").description("모집 상세 주소"),
                fieldWithPath("meetingAddressDong").description("모집 동 주소")
        );
        List<FieldDescriptor> successResponseDescriptors = List.of(
                fieldWithPath("meetingDate").description("마감시간"),
                fieldWithPath("meetingAddress").description("모집 주소"),
                fieldWithPath("meetingAddressDetail").description("모집 상세 주소"),
                fieldWithPath("meetingAddressDong").description("모집 동 주소")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("공모 일정 수정")
                .description("공모 id를 통해 공모의 일정 정보를 수정합니다.")
                .pathParameters(pathParameterDescriptors)
                .requestFields(requestDescriptors)
                .responseFields(successResponseDescriptors)
                .requestSchema(schema("OfferingMeetingUpdateSuccessRequest"))
                .responseSchema(schema("OfferingMeetingUpdateSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .summary("공모 일정 수정")
                .description("공모 id를 통해 공모의 일정 정보를 수정합니다.")
                .pathParameters(pathParameterDescriptors)
                .requestFields(requestDescriptors)
                .responseFields(failResponseDescriptors)
                .requestSchema(schema("OfferingMeetingUpdateFailRequest"))
                .responseSchema(schema("OfferingMeetingUpdateFailResponse"))
                .build();

        MemberEntity proposer;
        MemberEntity participant;
        OfferingEntity offering;

        @BeforeEach
        void setUp() {
            proposer = memberFixture.createMember("ever");
            participant = memberFixture.createMember("eber");
            offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);
        }

        @DisplayName("총대는 공모 id로 공모 일정 정보를 수정할 수 있다")
        @Test
        void should_updateSuccess_when_givenOfferingIdAndInfo() {
            OfferingMeetingUpdateRequest request = new OfferingMeetingUpdateRequest(
                    LocalDateTime.of(3000, 1, 1, 0, 0, 0),
                    "서울시 관악구 장군봉길1로",
                    "123동 123호",
                    "봉천동"
            );

            given(spec).log().all()
                    .filter(document("update-offering-meeting-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .pathParam("offering-id", offering.getId())
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().patch("/offerings/{offering-id}/meetings")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("총대가 아닌 참여자가 공모의 일정 정보를 수정할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_notProposer() {
            OfferingMeetingUpdateRequest request = new OfferingMeetingUpdateRequest(
                    LocalDateTime.of(3000, 1, 1, 0, 0, 0),
                    "서울시 관악구 장군봉길1로",
                    "123동 123호",
                    "봉천동"
            );

            given(spec).log().all()
                    .filter(document("update-offering-meeting-fail-not-proposer", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(participant))
                    .pathParam("offering-id", offering.getId())
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().patch("/offerings/{offering-id}/meetings")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("유효하지 않은 공모의 일정 정보를 수정할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidOffering() {
            OfferingMeetingUpdateRequest request = new OfferingMeetingUpdateRequest(
                    LocalDateTime.of(3000, 1, 1, 0, 0, 0),
                    "서울시 관악구 장군봉길1로",
                    "123동 123호",
                    "봉천동"
            );

            given(spec).log().all()
                    .filter(document("update-offering-meeting-fail-invalid-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .pathParam("offering-id", offering.getId() + 10000)
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().patch("/offerings/{offering-id}/meetings")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("공모 필터 목록 조회")
    @Nested
    class GetAllOfferingFilter {

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
                .responseSchema(schema("OfferingFilterSuccessResponse"))
                .build();

        private MemberEntity member;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("dora");
        }

        @DisplayName("공모 id로 공모 일정 정보를 조회할 수 있다")
        @Test
        void should_responseOfferingFilter_when_givenOfferingId() {
            given(spec).log().all()
                    .filter(document("get-all-offering-filter-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .when().get("/offerings/filters")
                    .then().log().all()
                    .statusCode(200);
        }
    }

    @DisplayName("공모 작성")
    @Nested
    class CreateOffering {

        List<FieldDescriptor> requestDescriptors = List.of(
                fieldWithPath("title").description("제목 (필수)"),
                fieldWithPath("productUrl").description("물품 구매 링크"),
                fieldWithPath("thumbnailUrl").description("사진 링크"),
                fieldWithPath("totalCount").description("총원 (필수)"),
                fieldWithPath("totalPrice").description("총가격 (필수)"),
                fieldWithPath("originPrice").description("원 가격"),
                fieldWithPath("meetingAddress").description("모집 주소 (필수)"),
                fieldWithPath("meetingAddressDetail").description("모집 상세 주소"),
                fieldWithPath("meetingAddressDong").description("모집 동 주소"),
                fieldWithPath("meetingDate").description("모집 종료 시간 (필수)"),
                fieldWithPath("description").description("내용 (필수)")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("공모 작성")
                .description("공모 정보를 받아 공모를 작성합니다.")
                .requestFields(requestDescriptors)
                .requestSchema(schema("OfferingCreateRequest"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .summary("공모 작성")
                .description("공모 정보를 받아 공모를 작성합니다.")
                .requestFields(requestDescriptors)
                .responseFields(failResponseDescriptors)
                .requestSchema(schema("OfferingCreateRequest"))
                .responseSchema(schema("OfferingCreateFailResponse"))
                .build();

        MemberEntity member;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("dora");
        }

        @DisplayName("공모 정보를 받아 공모를 작성 할 수 있다.")
        @Test
        void should_createOffering_when_givenOfferingCreateRequest() {
            OfferingSaveRequest request = new OfferingSaveRequest(
                    "공모 제목",
                    "www.naver.com",
                    "www.naver.com/favicon.ico",
                    5,
                    10000,
                    5000,
                    "서울특별시 광진구 구의강변로 3길 11",
                    "상세주소아파트",
                    "구의동",
                    LocalDateTime.now(clock).plusDays(1),
                    "내용입니다."
            );

            given(spec).log().all()
                    .filter(document("create-offering-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/offerings")
                    .then().log().all()
                    .statusCode(201);
        }

        @DisplayName("원가격을 작성하지 않은 공모 정보를 받아 공모를 작성 할 수 있다.")
        @Test
        void should_createOffering_when_givenOfferingWithoutOriginPriceCreateRequest() {
            OfferingSaveRequest request = new OfferingSaveRequest(
                    "공모 제목",
                    "www.naver.com",
                    "www.naver.com/favicon.ico",
                    5,
                    10000,
                    null,
                    "서울특별시 광진구 구의강변로 3길 11",
                    "상세주소아파트",
                    "구의동",
                    LocalDateTime.now(clock).plusDays(1),
                    "내용입니다."
            );

            given(spec).log().all()
                    .filter(document("create-offering-success-without-originPrice", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/offerings")
                    .then().log().all()
                    .statusCode(201);
        }

        @DisplayName("요청 값에 빈값이 들어오는 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_emptyValue() {
            OfferingSaveRequest request = new OfferingSaveRequest(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            given(spec).log().all()
                    .filter(document("create-offering-fail-request-with-null", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/offerings")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("원가가 n빵 가격보다 작을 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_dividedPrice() {
            OfferingSaveRequest request = new OfferingSaveRequest(
                    "공모 제목",
                    "www.naver.com",
                    "www.naver.com/favicon.ico",
                    3,
                    10000,
                    2000,
                    "서울특별시 광진구 구의강변로 3길 11",
                    "상세주소아파트",
                    "구의동",
                    LocalDateTime.parse("2024-10-11T10:00:00"),
                    "내용입니다."
            );

            given(spec).log().all()
                    .filter(document("create-offering-fail-with-invalid-originPrice", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/offerings")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("최대로 설정 가능한 공모 모집 인원수를 초과하는 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_overMaximumTotalCount() {
            OfferingSaveRequest request = new OfferingSaveRequest(
                    "공모 제목",
                    "www.naver.com",
                    "www.naver.com/favicon.ico",
                    999999999,
                    10000,
                    2000,
                    "서울특별시 광진구 구의강변로 3길 11",
                    "상세주소아파트",
                    "구의동",
                    LocalDateTime.parse("2024-10-11T10:00:00"),
                    "내용입니다."
            );

            given(spec).log().all()
                    .filter(document("create-offering-fail-with-invalid-totalCount", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/offerings")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("거래 날짜를 오늘보다 과거로 설정하는 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_meetingDateBeforeTomorrow() {
            OfferingSaveRequest request = new OfferingSaveRequest(
                    "공모 제목",
                    "www.naver.com",
                    "www.naver.com/favicon.ico",
                    10,
                    10000,
                    2000,
                    "서울특별시 광진구 구의강변로 3길 11",
                    "상세주소아파트",
                    "구의동",
                    LocalDateTime.now(clock).minusDays(1),
                    "내용입니다."
            );

            given(spec).log().all()
                    .filter(document("create-offering-fail-with-invalid-meeting-date", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/offerings")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("상품 이미지 추출")
    @Nested
    class ExtractProductImage {

        List<FieldDescriptor> requestDescriptors = List.of(
                fieldWithPath("productUrl").description("상품 url (필수)")
        );
        List<FieldDescriptor> responseDescriptors = List.of(
                fieldWithPath("imageUrl").description("이미지 url")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("상품 이미지 추출")
                .description("상품 링크를 받아 이미지를 추출합니다.")
                .requestFields(requestDescriptors)
                .responseFields(responseDescriptors)
                .requestSchema(schema("OfferingProductImageSuccessRequest"))
                .responseSchema(schema("OfferingProductImageSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .summary("상품 이미지 추출")
                .description("상품 링크를 받아 이미지를 추출합니다.")
                .requestFields(requestDescriptors)
                .responseFields(failResponseDescriptors)
                .requestSchema(schema("OfferingProductImageRequest"))
                .responseSchema(schema("OfferingProductImageResponse"))
                .build();

        private MemberEntity member;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("dora");
        }

        @DisplayName("상품 링크를 받아 이미지를 추출합니다.")
        @Test
        void should_extractImageUrl_when_givenProductUrl() {
            OfferingProductImageRequest request = new OfferingProductImageRequest("http://product-url.com");

            given(spec).log().all()
                    .filter(document("extract-product-image-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/offerings/product-images/og")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("상품 링크가 올바르지 않거나 이미지가 존재하지 않을 경우 빈 값을 반환합니다.")
        @Test
        void should_returnEmptyString_when_fail() {
            OfferingProductImageRequest request = new OfferingProductImageRequest("http://fail-product-url.com");

            given(spec).log().all()
                    .filter(document("extract-product-image-fail", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/offerings/product-images/og")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("요청 값에 빈값이 들어오는 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_emptyValue() {
            OfferingProductImageRequest request = new OfferingProductImageRequest(null);

            given(spec).log().all()
                    .filter(document("extract-product-image-fail-request-with-null", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/offerings/product-images/og")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("S3에 이미지 업로드")
    @Nested
    class UploadProductImage {

        List<FieldDescriptor> responseDescriptors = List.of(
                fieldWithPath("imageUrl").description("이미지 url")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("상품 이미지 업로드")
                .description("""
                        상품 이미지를 받아 이미지를 S3에 업로드한다.
                        현재 사용 플러그인이 multipart/form-data의 파라미터에 대한 문서화를 지원하지 않습니다.
                        ### Parameters
                        | Part         | Type   | Description            |
                        |--------------|--------|------------------------|
                        | image        | binary | 상품 이미지               |
                        """)
                .responseFields(responseDescriptors)
                .responseSchema(schema("OfferingProductImageSuccessResponse"))
                .build();

        private File image;
        private MemberEntity member;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("dora");
            image = new File("src/test/resources/test-image.png");
            MultipartFile mockImage = new MockMultipartFile("emptyImageFile", new byte[0]);
            given(storageService.uploadFile(mockImage, "path"))
                    .willReturn("https://uploaded-image-url.com");
        }

        @DisplayName("상품 이미지를 받아 이미지를 S3에 업로드한다.")
        @Test
        void should_uploadImageUrl_when_givenImageFile() {
            given(spec).log().all()
                    .filter(document("upload-product-image-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .multiPart("image", image)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .when().post("/offerings/product-images/s3")
                    .then().log().all()
                    .statusCode(200);
        }
    }

    @DisplayName("공모 수정")
    @Nested
    class UpdateOffering {

        List<ParameterDescriptorWithType> pathParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id (필수)")
        );

        List<FieldDescriptor> requestDescriptors = List.of(
                fieldWithPath("title").description("제목 (필수)"),
                fieldWithPath("productUrl").description("물품 구매 링크"),
                fieldWithPath("thumbnailUrl").description("사진 링크"),
                fieldWithPath("totalCount").description("총원 (필수)"),
                fieldWithPath("totalPrice").description("총가격 (필수)"),
                fieldWithPath("originPrice").description("원 가격"),
                fieldWithPath("meetingAddress").description("모집 주소 (필수)"),
                fieldWithPath("meetingAddressDetail").description("모집 상세 주소"),
                fieldWithPath("meetingAddressDong").description("모집 동 주소"),
                fieldWithPath("meetingDate").description("모집 종료 시간 (필수)"),
                fieldWithPath("description").description("내용 (필수)")
        );

        List<FieldDescriptor> successResponseDescriptors = List.of(
                fieldWithPath("id").description("공모 id"),
                fieldWithPath("title").description("제목"),
                fieldWithPath("productUrl").description("물품 링크"),
                fieldWithPath("meetingAddress").description("모집 주소"),
                fieldWithPath("meetingAddressDetail").description("모집 상세 주소"),
                fieldWithPath("description").description("내용"),
                fieldWithPath("meetingDate").description("마감시간"),
                fieldWithPath("currentCount").description("현재원"),
                fieldWithPath("totalCount").description("총원"),
                fieldWithPath("thumbnailUrl").description("사진 링크"),
                fieldWithPath("dividedPrice").description("n빵 가격"),
                fieldWithPath("totalPrice").description("총가격"),
                fieldWithPath("status").description("공모 상태"
                        + getEnumValuesAsString(OfferingStatus.class)),
                fieldWithPath("memberId").description("공모자 회원 id"),
                fieldWithPath("nickname").description("공모자 회원 닉네임")
        );

        ResourceSnippetParameters successSnippets = builder()
                .summary("공모 수정")
                .description("공모 정보를 받아 공모를 수정합니다.")
                .pathParameters(pathParameterDescriptors)
                .requestFields(requestDescriptors)
                .responseFields(successResponseDescriptors)
                .requestSchema(schema("OfferingUpdateRequest"))
                .build();

        ResourceSnippetParameters failSnippets = builder()
                .summary("공모 수정")
                .description("공모 정보를 받아 공모를 수정합니다.")
                .requestFields(requestDescriptors)
                .responseFields(failResponseDescriptors)
                .requestSchema(schema("OfferingUpdateRequest"))
                .responseSchema(schema("OfferingUpdateFailResponse"))
                .build();

        MemberEntity proposer;
        MemberEntity otherMember;

        @BeforeEach
        void setUp() {
            proposer = memberFixture.createMember("poke");
            otherMember = memberFixture.createMember("other");
            OfferingEntity offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);
            List<MemberEntity> participants = memberFixture.createMembers(9);
            participants.forEach(participant -> offeringMemberFixture.createParticipant(participant, offering));
        }

        @DisplayName("공모를 수정할 수 있다.")
        @Test
        void should_updateOffering_when_givenOfferingId() {
            OfferingUpdateRequest request = new OfferingUpdateRequest(
                    "수정할 제목",
                    "https://to.be.updated/productUrl",
                    "https://to.be.updated/thumbnail/url",
                    20,
                    20000,
                    5000,
                    "수정할 모집 장소 주소",
                    "수정할 모집 상세 주소",
                    "수정된동",
                    LocalDateTime.parse("2099-10-25T00:00:00"),
                    "수정할 공모 상세 내용"
            );

            given(spec).log().all()
                    .filter(document("update-offering-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .contentType(ContentType.JSON)
                    .pathParam("offering-id", 1)
                    .body(request)
                    .when().patch("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("제안자가 아닌 사용자가 공모를 수정할 수 없다.")
        @Test
        void should_throwException_when_updateOtherMember() {
            OfferingUpdateRequest request = new OfferingUpdateRequest(
                    "수정할 제목",
                    "https://to.be.updated/productUrl",
                    "https://to.be.updated/thumbnail/url",
                    20,
                    20000,
                    5000,
                    "수정할 모집 장소 주소",
                    "수정할 모집 상세 주소",
                    "수정된동",
                    LocalDateTime.parse("2024-10-25T00:00:00"),
                    "수정할 공모 상세 내용"
            );

            given(spec).log().all()
                    .filter(document("update-offering-fail-not-proposer", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(otherMember))
                    .contentType(ContentType.JSON)
                    .pathParam("offering-id", 1)
                    .body(request)
                    .when().patch("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("참여 인원 이하 인원으로 공모를 수정할 수 없다.")
        @Test
        void should_throwException_when_updateTotalCountLessEqualThanCurrentCount() {
            OfferingUpdateRequest request = new OfferingUpdateRequest(
                    "수정할 제목",
                    "https://to.be.updated/productUrl",
                    "https://to.be.updated/thumbnail/url",
                    9,
                    20000,
                    5000,
                    "수정할 모집 장소 주소",
                    "수정할 모집 상세 주소",
                    "수정된동",
                    LocalDateTime.parse("2024-10-25T00:00:00"),
                    "수정할 공모 상세 내용"
            );

            given(spec).log().all()
                    .filter(document("update-offering-fail-less-than-current-count", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .contentType(ContentType.JSON)
                    .pathParam("offering-id", 1)
                    .body(request)
                    .when().patch("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("모집 날짜가 지날 경우 수정할 수 없다.")
        @Test
        void should_throwException_when_modifyMeetingDateBeforeNowToday() {
            OfferingUpdateRequest request = new OfferingUpdateRequest(
                    "수정할 제목",
                    "https://to.be.updated/productUrl",
                    "https://to.be.updated/thumbnail/url",
                    20,
                    20000,
                    5000,
                    "수정할 모집 장소 주소",
                    "수정할 모집 상세 주소",
                    "수정된동",
                    LocalDateTime.now().minusDays(1),
                    "수정할 공모 상세 내용"
            );

            given(spec).log().all()
                    .filter(document("update-offering-fail-before-now-today", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .contentType(ContentType.JSON)
                    .pathParam("offering-id", 1)
                    .body(request)
                    .when().patch("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("수정 할 원가격이 N빵 가격보다 작을경우 수정할 수 없다.")
        @Test
        void should_throwException_when_originPriceLessThanDividePrice() {
            OfferingUpdateRequest request = new OfferingUpdateRequest(
                    "수정할 제목",
                    "https://to.be.updated/productUrl",
                    "https://to.be.updated/thumbnail/url",
                    20,
                    20000,
                    500,
                    "수정할 모집 장소 주소",
                    "수정할 모집 상세 주소",
                    "수정된동",
                    LocalDateTime.parse("2024-10-25T00:00:00"),
                    "수정할 공모 상세 내용"
            );

            given(spec).log().all()
                    .filter(document("patch-offering-fail-less-than-divide-price", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .contentType(ContentType.JSON)
                    .pathParam("offering-id", 1)
                    .body(request)
                    .when().patch("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("공모 삭제")
    @Nested
    class DeleteOffering {

        List<ParameterDescriptorWithType> pathParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id (필수)")
        );

        ResourceSnippetParameters successSnippets = builder()
                .summary("공모 삭제")
                .description("공모 id를 통해 공모를 삭제합니다.")
                .pathParameters(pathParameterDescriptors)
                .responseSchema(schema("OfferingDeleteSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .summary("공모 삭제")
                .description("공모 id를 통해 공모를 삭제합니다.")
                .pathParameters(pathParameterDescriptors)
                .responseFields(failResponseDescriptors)
                .responseSchema(schema("OfferingDeleteFailResponse"))
                .build();

        MemberEntity proposer;
        MemberEntity notProposer;
        MemberEntity participant;
        OfferingEntity offering;
        OfferingEntity offeringInProgress;
        OfferingEntity offeringDone;

        @BeforeEach
        void setUp() {
            notProposer = memberFixture.createMember("never");
            proposer = memberFixture.createMember("ever");
            offering = offeringFixture.createOffering(proposer);
            participant = memberFixture.createMember("naver");
            offeringMemberFixture.createParticipant(participant, offering);
            offeringInProgress = offeringFixture.createOffering(proposer, CommentRoomStatus.TRADING);
            offeringDone = offeringFixture.createOffering(proposer, CommentRoomStatus.DONE);
        }

        @DisplayName("공모 id로 공모를 삭제할 수 있다")
        @Test
        void should_deleteOffering_when_givenOfferingId() {
            given(spec).log().all()
                    .filter(document("delete-offering-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .pathParam("offering-id", offering.getId())
                    .when().delete("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(204);
        }

        @DisplayName("유효하지 않은 공모 삭제를 시도할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidOffering() {
            given(spec).log().all()
                    .filter(document("delete-offering-fail-invalid-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .pathParam("offering-id", offering.getId() + 9999)
                    .when().delete("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("총대가 아닌 사용자가 공모 삭제를 시도할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_notProposer() {
            given(spec).log().all()
                    .filter(document("delete-offering-fail-not-proposer", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(notProposer))
                    .pathParam("offering-id", offering.getId())
                    .when().delete("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("참여자가 공모 삭제를 시도할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_participant() {
            given(spec).log().all()
                    .filter(document("delete-offering-fail-participant", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(participant))
                    .pathParam("offering-id", offering.getId())
                    .when().delete("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("거래 진행 중 삭제를 시도할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_unavailableStatus() {
            given(spec).log().all()
                    .filter(document("delete-offering-fail-in-progress", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .pathParam("offering-id", offeringInProgress.getId())
                    .when().delete("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("거래가 완료된 공모를 삭제할 수 있다.")
        @Test
        void should_deleteOffering_when_givenDoneOffering() {
            given().log().all()
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .pathParam("offering-id", offeringDone.getId())
                    .when().delete("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(204);
        }
    }
}
