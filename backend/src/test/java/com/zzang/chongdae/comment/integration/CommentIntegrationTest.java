package com.zzang.chongdae.comment.integration;

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

public class CommentIntegrationTest extends IntegrationTest {

    @DisplayName("댓글 작성")
    @Nested
    class SaveComment {

        List<FieldDescriptor> saveCommentRequestDescriptors = List.of(
                fieldWithPath("memberId").description("회원 id"),
                fieldWithPath("offeringId").description("공모 id"),
                fieldWithPath("content").description("내용")
        );
        ResourceSnippetParameters snippets = builder()
                .summary("댓글 작성")
                .description("댓글을 작성합니다.")
                .requestFields(saveCommentRequestDescriptors)
                .requestSchema(schema("CommentSaveRequest"))
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
            Map<String, String> request = Map.of(
                    "memberId", member.getId().toString(),
                    "offeringId", offering.getId().toString(),
                    "content", "댓글 내용"
            );

            RestAssured.given(spec).log().all()
                    .filter(document("save-comment-success", resource(snippets)))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/comments")
                    .then().log().all()
                    .statusCode(200);
        }
    }

    @DisplayName("댓글방 목록 조회")
    @Nested
    class GetAllCommentRoom {

        List<ParameterDescriptorWithType> getAllCommentRoomQueryParameterDescriptors = List.of(
                parameterWithName("member-id").description("회원 id")
        );
        List<FieldDescriptor> getAllCommentRoomResponseDescriptors = List.of(
                fieldWithPath("offerings[].offeringId").description("공모 id"),
                fieldWithPath("offerings[].offeringTitle").description("공모 제목"),
                fieldWithPath("offerings[].latestComment.content").description("최신 댓글 내용"),
                fieldWithPath("offerings[].latestComment.createdAt").description("최신 댓글 작성일"),
                fieldWithPath("offerings[].isProposer").description("총대 여부")
        );
        ResourceSnippetParameters snippets = builder()
                .summary("댓글방 목록 조회")
                .description("댓글방 목록을 조회합니다.")
                .queryParameters(getAllCommentRoomQueryParameterDescriptors)
                .responseFields(getAllCommentRoomResponseDescriptors)
                .responseSchema(schema("CommentRoomAllResponse"))
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
                    .filter(document("get-all-comment-room-success", resource(snippets)))
                    .queryParam("member-id", member.getId())
                    .when().get("/comments")
                    .then().log().all()
                    .statusCode(200);
        }
    }

    @DisplayName("댓글 목록 조회")
    @Nested
    class GetAllComment {

        List<ParameterDescriptorWithType> getAllCommentPathParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id")
        );
        List<ParameterDescriptorWithType> getAllCommentQueryParameterDescriptors = List.of(
                parameterWithName("member-id").description("회원 id")
        );
        List<FieldDescriptor> getAllCommentResponseDescriptors = List.of(
                fieldWithPath("comments[].createdAt.date").description("작성 날짜"),
                fieldWithPath("comments[].createdAt.time").description("작성 시간"),
                fieldWithPath("comments[].content").description("내용"),
                fieldWithPath("comments[].nickname").description("작성자 회원 닉네임"),
                fieldWithPath("comments[].isProposer").description("총대 여부"),
                fieldWithPath("comments[].isMine").description("내 댓글 여부")
        );
        ResourceSnippetParameters snippets = builder()
                .summary("댓글 목록 조회")
                .description("댓글 목록을 조회합니다.")
                .pathParameters(getAllCommentPathParameterDescriptors)
                .queryParameters(getAllCommentQueryParameterDescriptors)
                .responseFields(getAllCommentResponseDescriptors)
                .responseSchema(schema("CommentAllResponse"))
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
                    .filter(document("get-all-comment-success", resource(snippets)))
                    .pathParam("offering-id", offering.getId())
                    .queryParam("member-id", member.getId())
                    .when().get("/comments/{offering-id}")
                    .then().log().all()
                    .statusCode(200);
        }
    }
}
