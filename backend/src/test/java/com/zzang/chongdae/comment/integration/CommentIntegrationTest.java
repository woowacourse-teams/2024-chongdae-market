package com.zzang.chongdae.comment.integration;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.Schema.schema;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.zzang.chongdae.comment.domain.SearchDirection;
import com.zzang.chongdae.comment.service.dto.CommentSaveRequest;
import com.zzang.chongdae.global.integration.IntegrationTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;

public class CommentIntegrationTest extends IntegrationTest {

    @DisplayName("댓글 작성")
    @Nested
    class SaveComment {

        List<FieldDescriptor> requestDescriptors = List.of(
                fieldWithPath("offeringId").description("공모 id (필수)"),
                fieldWithPath("content").description("내용 (필수)")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("댓글 작성")
                .description("댓글을 작성합니다.")
                .requestFields(requestDescriptors)
                .requestSchema(schema("CommentSaveRequest"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .responseFields(failResponseDescriptors)
                .responseSchema(schema("CommentSaveFailResponse")) // TODO: 중복되는 값 제거
                .build();

        MemberEntity member;
        OfferingEntity offering;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("dora");
            offering = offeringFixture.createOffering(member);
            offeringMemberFixture.createProposer(member, offering);
        }

        @DisplayName("댓글을 작성할 수 있다")
        @Test
        void should_saveCommentSuccess_when_givenCommentSaveRequest() {
            CommentSaveRequest request = new CommentSaveRequest(
                    offering.getId(),
                    "댓글 내용"
            );

            RestAssured.given(spec).log().all()
                    .filter(document("save-comment-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/comments")
                    .then().log().all()
                    .statusCode(201);
        }

        @DisplayName("요청 값에 빈값이 들어오는 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_emptyValue() {
            CommentSaveRequest request = new CommentSaveRequest(
                    null,
                    ""
            );

            RestAssured.given(spec).log().all()
                    .filter(document("save-comment-fail-request-with-null", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/comments")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("요청 받은 내용의 길이가 80이 넘어가는 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_longContent() {
            CommentSaveRequest request = new CommentSaveRequest(
                    offering.getId(),
                    "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
            );

            RestAssured.given(spec).log().all()
                    .filter(document("save-comment-fail-request-with-long-content", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/comments")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("유효하지 않은 공모에 대해 작성을 시도하는 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidOffering() {
            CommentSaveRequest request = new CommentSaveRequest(
                    offering.getId() + 100,
                    "댓글 내용"
            );

            RestAssured.given(spec).log().all()
                    .filter(document("save-comment-fail-invalid-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/comments")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("댓글방 목록 조회")
    @Nested
    class GetAllCommentRoom {

        List<FieldDescriptor> successResponseDescriptors = List.of(
                fieldWithPath("offerings[].offeringId").description("공모 id"),
                fieldWithPath("offerings[].offeringTitle").description("공모 제목"),
                fieldWithPath("offerings[].latestComment.content").description("최신 댓글 내용"),
                fieldWithPath("offerings[].latestComment.createdAt").description("최신 댓글 작성일"),
                fieldWithPath("offerings[].isProposer").description("총대 여부")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("댓글방 목록 조회")
                .description("댓글방 목록을 조회합니다.")
                .responseFields(successResponseDescriptors)
                .responseSchema(schema("CommentRoomAllSuccessResponse"))
                .build();

        MemberEntity member;
        OfferingEntity offering;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("dora");
            offering = offeringFixture.createOffering(member);
            offeringMemberFixture.createProposer(member, offering);
            MemberEntity participant = memberFixture.createMember("참여자");
            offeringMemberFixture.createParticipant(participant, offering);
            commentFixture.createComment(member, offering);
        }

        @DisplayName("댓글방 목록을 조회할 수 있다")
        @Test
        void should_responseAllCommentRoom_when_givenMemberId() {
            RestAssured.given(spec).log().all()
                    .filter(document("get-all-comment-room-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .when().get("/comments")
                    .then().log().all()
                    .statusCode(200);
        }
    }

    @DisplayName("댓글방 정보 조회")
    @Nested
    class GetCommentRoomInfo {

        List<ParameterDescriptorWithType> queryParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id (필수)")
        );
        List<FieldDescriptor> successResponseDescriptors = List.of(
                fieldWithPath("status").description("상태"),
                fieldWithPath("imageUrl").description("이미지 url"),
                fieldWithPath("buttonText").description("버튼 text"),
                fieldWithPath("message").description("alert 메시지"),
                fieldWithPath("title").description("공모글 제목"),
                fieldWithPath("isProposer").description("로그인 사용자의 총대 여부")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("댓글방 정보 조회")
                .description("공모 id를 통해 댓글방의 정보를 조회합니다.")
                .queryParameters(queryParameterDescriptors)
                .responseFields(successResponseDescriptors)
                .responseSchema(schema("CommentRoomInfoSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .summary("댓글방 정보 조회")
                .description("공모 id를 통해 댓글방의 정보를 조회합니다.")
                .queryParameters(queryParameterDescriptors)
                .responseFields(failResponseDescriptors)
                .responseSchema(schema("CommentRoomInfoFailResponse"))
                .build();

        OfferingEntity offering;
        MemberEntity member;
        MemberEntity invalidMember;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("ever");
            invalidMember = memberFixture.createMember("invalid");
            offering = offeringFixture.createOffering(member);
            offeringMemberFixture.createProposer(member, offering);
        }

        @DisplayName("공모 id로 댓글방 정보를 조회할 수 있다")
        @Test
        void should_responseCommentRoomInfo_when_givenOfferingId() {
            given(spec).log().all()
                    .filter(document("get-comment-room-info-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .queryParam("offering-id", offering.getId())
                    .when().get("/comments/info")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("존재하지 않는 공모 id로 댓글방 정보를 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_givenInvalidOffering() {
            given(spec).log().all()
                    .filter(document("get-comment-room-info-fail-invalid-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .queryParam("offering-id", offering.getId() + 100)
                    .when().get("/comments/info")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("총대 혹은 참여자가 아닌 사용자가 댓글방 정보를 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_givenInvalidMember() {
            given(spec).log().all()
                    .filter(document("get-comment-room-info-fail-invalid-member", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(invalidMember))
                    .queryParam("offering-id", offering.getId())
                    .when().get("/comments/info")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("댓글방 상태 변경")
    @Nested
    class UpdateCommentRoomStatus {

        List<ParameterDescriptorWithType> queryParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id (필수)")
        );
        List<FieldDescriptor> successResponseDescriptors = List.of(
                fieldWithPath("updatedStatus").description("변경된 상태")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("댓글방 상태 변경")
                .description("댓글방의 상태를 변경합니다.")
                .queryParameters(queryParameterDescriptors)
                .responseFields(successResponseDescriptors)
                .responseSchema(schema("CommentRoomStatusUpdateSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .summary("댓글방 상태 변경")
                .description("댓글방의 상태를 변경합니다.")
                .queryParameters(queryParameterDescriptors)
                .responseFields(failResponseDescriptors)
                .responseSchema(schema("CommentRoomStatusUpdateFailResponse"))
                .build();
        MemberEntity member;
        MemberEntity invalidMember;
        OfferingEntity offering;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("ever");
            invalidMember = memberFixture.createMember("invalid");
            offering = offeringFixture.createOffering(member);
            offeringMemberFixture.createProposer(member, offering);
            commentFixture.createComment(member, offering);
        }

        @DisplayName("댓글방 상태를 다음 상태로 변경할 수 있다.")
        @Test
        void should_updateStatus_when_givenOfferingIdAndMemberId() {
            RestAssured.given(spec).log().all()
                    .filter(document("update-comment-room-status-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .queryParam("offering-id", offering.getId())
                    .when().patch("/comments/status")
                    .then().log().all()
                    .statusCode(200);

            RestAssured.given().log().all()
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .queryParam("offering-id", offering.getId())
                    .when().get("/comments/info")
                    .then().log().all()
                    .statusCode(200)
                    .body("status", is("BUYING"));
        }

        @DisplayName("유효하지 않은 공모에 대해 댓글방 상태를 변경할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidOffering() {
            RestAssured.given(spec).log().all()
                    .filter(document("update-comment-room-status-fail-invalid-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member))
                    .queryParam("offering-id", offering.getId() + 100)
                    .when().patch("/comments/status")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("총대가 아닌 사용자가 댓글방 상태를 변경할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidMember() {
            RestAssured.given(spec).log().all()
                    .filter(document("update-comment-room-status-fail-invalid-member", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(invalidMember))
                    .queryParam("offering-id", offering.getId() + 100)
                    .when().patch("/comments/status")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("댓글 목록 조회")
    @Nested
    class GetAllComment {

        List<ParameterDescriptorWithType> queryParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id (필수)"),
                parameterWithName("direction").description("last-id 기준으로 조회할 방향. 기본값 : PREVIOUS, 방향 옵션 : "
                        + getEnumValuesAsString(SearchDirection.class)).optional(),
                parameterWithName("last-id").description("마지막 댓글 id, 기본값 : comment 마지막 id + 1").optional(),
                parameterWithName("page-size").description("페이지 크기 (기본값: 100, 최대: 100, 최소 1)").optional()
        );
        List<FieldDescriptor> successResponseDescriptors = List.of(
                fieldWithPath("comments[].commentId").description("댓글 id"),
                fieldWithPath("comments[].createdAt.date").description("작성 날짜"),
                fieldWithPath("comments[].createdAt.time").description("작성 시간"),
                fieldWithPath("comments[].content").description("내용"),
                fieldWithPath("comments[].nickname").description("작성자 회원 닉네임"),
                fieldWithPath("comments[].isProposer").description("총대 여부"),
                fieldWithPath("comments[].isMine").description("내 댓글 여부")
        );
        ResourceSnippetParameters successSnippets = builder()
                .summary("댓글 목록 조회")
                .description("댓글 목록을 조회합니다.")
                .queryParameters(queryParameterDescriptors)
                .responseFields(successResponseDescriptors)
                .responseSchema(schema("CommentAllSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .summary("댓글 목록 조회")
                .description("댓글 목록을 조회합니다.")
                .queryParameters(queryParameterDescriptors)
                .responseFields(failResponseDescriptors)
                .responseSchema(schema("CommentAllFailResponse"))
                .build();
        MemberEntity member1, member2, member3, member4, member5;
        OfferingEntity offering;

        @BeforeEach
        void setUp() {
            member1 = memberFixture.createMember("dora");
            member2 = memberFixture.createMember("ever");
            member3 = memberFixture.createMember("poke");
            member4 = memberFixture.createMember("mason");
            member5 = memberFixture.createMember("whatever");
            offering = offeringFixture.createOffering(member1);
            offeringMemberFixture.createProposer(member1, offering);
            offeringMemberFixture.createParticipant(member2, offering);
            offeringMemberFixture.createParticipant(member3, offering);
            offeringMemberFixture.createParticipant(member4, offering);
            offeringMemberFixture.createParticipant(member5, offering);
            commentFixture.createComment(member1, offering);
            commentFixture.createComment(member2, offering);
            commentFixture.createComment(member3, offering);
            commentFixture.createComment(member4, offering);
            commentFixture.createComment(member5, offering);
        }

        @DisplayName("댓글 목록을 조회할 때 옵션을 지정하지 않으면 최신 댓글 100개를 보여준다.")
        @Test
        void should_responseAllComment_when_givenOfferingIdAndMemberIdWithoutParameter() {
            RestAssured.given(spec).log().all()
                    .filter(document("get-all-comment-when-without-query-parameter", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member1))
                    .queryParam("offering-id", offering.getId())
                    .when().get("/comments/messages")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("last-id 기준으로 이전 댓글 목록을 보여준다.")
        @Test
        void should_responsePreviousComment_when_givenOfferingIdAndMemberIdAndLastId() {
            RestAssured.given(spec).log().all()
                    .filter(document("get-prev-comment-when-direction-previous", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member1))
                    .queryParam("offering-id", offering.getId())
                    .queryParam("last-id", 3)
                    .queryParam("direction", "PREVIOUS")
                    .when().get("/comments/messages")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("last-id 기준으로 다음 댓글 목록을 보여준다.")
        @Test
        void should_responseNextComment_when_givenOfferingIdAndMemberIdAndLastId() {
            RestAssured.given(spec).log().all()
                    .filter(document("get-next-comment-when-direction-next", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member1))
                    .queryParam("offering-id", offering.getId())
                    .queryParam("last-id", 3)
                    .queryParam("direction", "NEXT")
                    .when().get("/comments/messages")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("page-size룰 설정하여 가져오는 댓글 수를 조절할 수 있다.")
        @Test
        void should_responseAllComment_when_givenOfferingIdAndMemberIdAndPageSize() {
            RestAssured.given(spec).log().all()
                    .filter(document("get-all-comment-with-page-size", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member1))
                    .queryParam("offering-id", offering.getId())
                    .queryParam("page-size", 1)
                    .when().get("/comments/messages")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("유효하지 않은 공모에 대해 댓글을 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidOffering() {
            RestAssured.given(spec).log().all()
                    .filter(document("get-all-comment-fail-invalid-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(member1))
                    .queryParam("offering-id", offering.getId() + 100)
                    .when().get("/comments/messages")
                    .then().log().all()
                    .statusCode(400);
        }
    }
}
