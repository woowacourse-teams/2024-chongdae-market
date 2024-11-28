package com.zzang.chongdae.comment.integration;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
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
            member = memberFixture.createMember();
            offering = offeringFixture.createOffering(member);
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
                    .cookies(cookieProvider.createCookies())
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
                    .cookies(cookieProvider.createCookies())
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
                    .cookies(cookieProvider.createCookies())
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
                    .cookies(cookieProvider.createCookies())
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
            member = memberFixture.createMember();
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
                    .cookies(cookieProvider.createCookies())
                    .when().get("/comments")
                    .then().log().all()
                    .statusCode(200);
        }
    }

    @DisplayName("댓글 목록 조회")
    @Nested
    class GetAllComment {

        List<ParameterDescriptorWithType> pathParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id (필수)")
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
                .pathParameters(pathParameterDescriptors)
                .responseFields(successResponseDescriptors)
                .responseSchema(schema("CommentAllSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = builder()
                .summary("댓글 목록 조회")
                .description("댓글 목록을 조회합니다.")
                .pathParameters(pathParameterDescriptors)
                .responseFields(failResponseDescriptors)
                .responseSchema(schema("CommentAllFailResponse"))
                .build();
        MemberEntity member;
        OfferingEntity offering;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember();
            offering = offeringFixture.createOffering(member);
            offeringMemberFixture.createProposer(member, offering);
            commentFixture.createComment(member, offering);
        }

        @DisplayName("댓글 목록을 조회할 수 있다")
        @Test
        void should_responseAllComment_when_givenOfferingIdAndMemberId() {
            RestAssured.given(spec).log().all()
                    .filter(document("get-all-comment-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookies())
                    .pathParam("offering-id", offering.getId())
                    .when().get("/comments/{offering-id}")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("유효하지 않은 공모에 대해 댓글을 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidOffering() {
            RestAssured.given(spec).log().all()
                    .filter(document("get-all-comment-fail-invalid-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookies())
                    .pathParam("offering-id", offering.getId() + 100)
                    .when().get("/comments/{offering-id}")
                    .then().log().all()
                    .statusCode(400);
        }
    }
}
