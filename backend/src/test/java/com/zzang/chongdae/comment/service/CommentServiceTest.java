package com.zzang.chongdae.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zzang.chongdae.comment.service.dto.CommentRoomAllResponse;
import com.zzang.chongdae.comment.service.dto.CommentRoomAllResponseItem;
import com.zzang.chongdae.comment.service.dto.CommentRoomInfoResponse;
import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentServiceTest extends ServiceTest {

    @Autowired
    CommentService commentService;

    @DisplayName("댓글방 목록 조회")
    @Nested
    class GetAllCommentRoom {

        MemberEntity member;
        OfferingEntity firstOffering;
        OfferingEntity secondOffering;
        OfferingEntity thirdOffering;
        OfferingEntity fourthOffering;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("dora");
            firstOffering = offeringFixture.createOffering(member);
            secondOffering = offeringFixture.createOffering(member);
            thirdOffering = offeringFixture.createOffering(member);
            fourthOffering = offeringFixture.createOffering(member);
            offeringMemberFixture.createProposer(member, firstOffering);
            offeringMemberFixture.createProposer(member, secondOffering);
            offeringMemberFixture.createProposer(member, thirdOffering);
            offeringMemberFixture.createProposer(member, fourthOffering);
            commentFixture.createComment(member, firstOffering);
            commentFixture.createComment(member, secondOffering);
        }

        @DisplayName("로그인한 유저가 참여한 댓글방 목록을 조회할 수 있다")
        @Test
        void should_getAllCommentRoom_when_givenLoginMember() {
            // when
            CommentRoomAllResponse response = commentService.getAllCommentRoom(member);

            // then
            assertEquals(response.offerings().size(), 4);
        }

        @DisplayName("최근 댓글이 작성된 순으로 (댓글이 없으면 최근 참여 날짜로) 정렬해 댓글방 목록을 조회할 수 있다")
        @Test
        void should_getAllCommentRoomWithOrder_when_givenLoginMember() {
            // when
            CommentRoomAllResponse response = commentService.getAllCommentRoom(member);

            // then
            assertThat(response.offerings())
                    .extracting(CommentRoomAllResponseItem::offeringId)
                    .containsExactly(2L, 1L, 4L, 3L);
        }

        @DisplayName("댓글방 목록 조회 시 삭제된 공모에 대한 댓글방은 제목에 삭제되었다고 명시되어 있다")
        @Test
        void should_getAllCommentRoomWithDeletedCommentRoom_when_giveLoginMember() {
            // given
            offeringFixture.deleteOffering(firstOffering);

            // when
            CommentRoomAllResponse response = commentService.getAllCommentRoom(member);

            // then
            assertEquals(response.offerings().get(1).offeringTitle(), "삭제된 공동구매입니다.");
        }
    }

    @DisplayName("댓글방 정보 조회")
    @Nested
    class GetCommentRoomInfo {

        MemberEntity member;
        OfferingEntity offering;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("dora");
            offering = offeringFixture.createOffering(member);
            offeringMemberFixture.createProposer(member, offering);
        }

        @DisplayName("삭제되지 않은 공모 id를 통해 댓글방 상세 조회를 할 수 있다")
        @Test
        void should_getExistedCommentRoomInfo_when_givenOfferingId() {
            // when
            CommentRoomInfoResponse response = commentService.getCommentRoomInfo(offering.getId(), member);

            // then
            assertEquals(response.title(), offering.getTitle());
        }

        @DisplayName("삭제된 공모 id를 통해 삭제된 공모라고 명시된 댓글방 상세 조회를 할 수 있다")
        @Test
        void should_getDeletedCommentRoomInfo_when_givenDeletedOfferingId() {
            // given
            offeringFixture.deleteOffering(offering);

            // when
            CommentRoomInfoResponse response = commentService.getCommentRoomInfo(offering.getId(), member);

            // then
            assertEquals(response.status(), CommentRoomStatus.DELETED);
            assertEquals(response.title(), "삭제된 공동구매입니다.");
        }
    }
}
