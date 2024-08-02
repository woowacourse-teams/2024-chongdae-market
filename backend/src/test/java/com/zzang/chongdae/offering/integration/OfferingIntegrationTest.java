package com.zzang.chongdae.offering.integration;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.Schema.schema;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.mockito.BDDMockito.given;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.zzang.chongdae.global.integration.IntegrationTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.service.dto.OfferingProductImageRequest;
import com.zzang.chongdae.offering.service.dto.OfferingSaveRequest;
import com.zzang.chongdae.storage.service.StorageService;
import io.restassured.http.ContentType;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
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
                parameterWithName("offering-id").description("공모 id")
        );
        List<ParameterDescriptorWithType> queryParameterDescriptors = List.of(
                parameterWithName("member-id").description("회원 id")
        );
        List<FieldDescriptor> successResponseDescriptors = List.of(
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
        ResourceSnippetParameters successSnippets = builder()
                .summary("공모 상세 조회")
                .description("공모 id를 통해 공모의 상세 정보를 조회합니다.")
                .pathParameters(pathParameterDescriptors)
                .queryParameters(queryParameterDescriptors)
                .responseFields(successResponseDescriptors)
                .responseSchema(schema("OfferingDetailSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .summary("공모 상세 조회")
                .description("공모 id를 통해 공모의 상세 정보를 조회합니다.")
                .pathParameters(pathParameterDescriptors)
                .queryParameters(queryParameterDescriptors)
                .responseFields(failResponseDescriptors)
                .responseSchema(schema("OfferingDetailFailResponse"))
                .build();

        @BeforeEach
        void setUp() {
            MemberEntity member = memberFixture.createMember();
            offeringFixture.createOffering(member);
        }

        @DisplayName("공모 id로 공모 상세 정보를 조회할 수 있다")
        @Test
        void should_responseOfferingDetail_when_givenOfferingId() {
            given(spec).log().all()
                    .filter(document("get-offering-detail-success", resource(successSnippets)))
                    .pathParam("offering-id", 1)
                    .queryParam("member-id", 1)
                    .when().get("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("유효하지 않은 공모를 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidOffering() {
            given(spec).log().all()
                    .filter(document("get-offering-detail-fail-invalid-offering", resource(failSnippets)))
                    .pathParam("offering-id", 100)
                    .queryParam("member-id", 1)
                    .when().get("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("유효하지 않은 사용자가 공모를 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidMember() {
            given(spec).log().all()
                    .filter(document("get-offering-detail-fail-invalid-member", resource(failSnippets)))
                    .pathParam("offering-id", 1)
                    .queryParam("member-id", 100)
                    .when().get("/offerings/{offering-id}")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("공모 목록 조회")
    @Nested
    class GetAllOffering {

        List<ParameterDescriptorWithType> queryParameterDescriptors = List.of(
                parameterWithName("last-id").description("마지막 공모 id"),
                parameterWithName("page-size").description("페이지 크기")
        );
        List<FieldDescriptor> successResponseDescriptors = List.of(
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
        ResourceSnippetParameters successSnippets = builder()
                .summary("공모 목록 조회")
                .description("공모 목록을 조회합니다.")
                .queryParameters(queryParameterDescriptors)
                .responseFields(successResponseDescriptors)
                .responseSchema(schema("OfferingAllSuccessResponse"))
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
            given(spec).log().all()
                    .filter(document("get-all-offering-success", resource(successSnippets)))
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

        List<ParameterDescriptorWithType> pathParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id")
        );
        List<FieldDescriptor> successResponseDescriptors = List.of(
                fieldWithPath("deadline").description("마감시간"),
                fieldWithPath("meetingAddress").description("모집 주소"),
                fieldWithPath("meetingAddressDetail").description("모집 상세 주소")
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

        @BeforeEach
        void setUp() {
            MemberEntity member = memberFixture.createMember();
            offeringFixture.createOffering(member);
        }

        @DisplayName("공모 id로 공모 일정 정보를 조회할 수 있다")
        @Test
        void should_responseOfferingMeeting_when_givenOfferingId() {
            given(spec).log().all()
                    .filter(document("get-offering-meeting-success", resource(successSnippets)))
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
                    .pathParam("offering-id", 100)
                    .when().get("/offerings/{offering-id}/meetings")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("공모 작성")
    @Nested
    class CreateOffering {

        List<FieldDescriptor> requestDescriptors = List.of(
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
            member = memberFixture.createMember();
        }

        @DisplayName("공모 정보를 받아 공모를 작성합니다")
        @Test
        void should_createOffering_when_givenOfferingCreateRequest() {
            OfferingSaveRequest request = new OfferingSaveRequest(
                    member.getId(),
                    "공모 제목",
                    "www.naver.com",
                    "www.naver.com/favicon.ico",
                    5,
                    10000,
                    2000,
                    "서울특별시 광진구 구의강변로 3길 11",
                    "상세주소아파트",
                    "구의동",
                    LocalDateTime.parse("2024-10-11T10:00:00"),
                    "내용입니다."
            );

            given(spec).log().all()
                    .filter(document("create-offering-success", resource(successSnippets)))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/offerings")
                    .then().log().all()
                    .statusCode(201);
        }

        @DisplayName("유효하지 않은 사용자가 공모를 작성할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidMember() {
            OfferingSaveRequest request = new OfferingSaveRequest(
                    member.getId() + 100,
                    "공모 제목",
                    "www.naver.com",
                    "www.naver.com/favicon.ico",
                    5,
                    10000,
                    2000,
                    "서울특별시 광진구 구의강변로 3길 11",
                    "상세주소아파트",
                    "구의동",
                    LocalDateTime.parse("2024-10-11T10:00:00"),
                    "내용입니다."
            );

            given(spec).log().all()
                    .filter(document("create-offering-fail-invalid-member", resource(failSnippets)))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/offerings")
                    .then().log().all()
                    .statusCode(400);
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
                    null,
                    null
            );

            given(spec).log().all()
                    .filter(document("create-offering-fail-request-with-null", resource(failSnippets)))
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
                fieldWithPath("productUrl").description("상품 url")
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

        @DisplayName("상품 링크를 받아 이미지를 추출합니다.")
        @Test
        void should_extractImageUrl_when_givenProductUrl() {
            OfferingProductImageRequest request = new OfferingProductImageRequest("http://product-url.com");

            given(spec).log().all()
                    .filter(document("extract-product-image-success", resource(successSnippets)))
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

        List<ParameterDescriptorWithType> queryParameterDescriptors = List.of(
                parameterWithName("image").description("상품 이미지 파일")
        );
        List<FieldDescriptor> responseDescriptors = List.of(
                fieldWithPath("imageUrl").description("이미지 url")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("상품 이미지 압로드")
                .description("상품 이미지를 받아 이미지를 S3에 업로드한다.")
                .responseFields(responseDescriptors)
                .responseSchema(schema("OfferingProductImageSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .summary("상품 이미지 업로드")
                .description("상품 이미지를 받아 이미지를 S3에 업로드한다.")
                .responseFields(failResponseDescriptors)
                .responseSchema(schema("OfferingProductImageResponse"))
                .build();

        private MultipartFile emptyMultipartFile;

        @BeforeEach
        void setUp() {
            emptyMultipartFile = new MockMultipartFile("emptyImageFile", new byte[0]);
        }

        @DisplayName("상품 이미지를 받아 이미지를 S3에 업로드한다.")
        @Test
        void should_uploadImageUrl_when_givenImageFile() {

            File image = new File("src/test/resources/test-image.png");
            given(storageService.uploadFile(emptyMultipartFile, "path"))
                    .willReturn("");

            given(spec).log().all()
                    .filter(document("extract-product-image-success", resource(successSnippets)))
                    .multiPart("image", image)
                    .contentType(ContentType.MULTIPART)
                    .when().post("/offerings/product-images/s3")
                    .then().log().all()
                    .statusCode(200);
        }
    }
}
