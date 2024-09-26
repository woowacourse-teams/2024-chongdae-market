package com.zzang.chongdae.comment.integration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zzang.chongdae.comment.service.CommentService;
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

    @DisplayName("채팅방 상세 조회")
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

        @DisplayName("삭제되지 않은 공모 id를 통해 채팅방 상세 조회를 할 수 있다")
        @Test
        void should_getExistedCommentRoomInfo_when_givenOfferingId() {
            // when
            CommentRoomInfoResponse response = commentService.getCommentRoomInfo(offering.getId(), member);

            // then
            assertEquals(response.title(), offering.getTitle());
        }

        @DisplayName("삭제된 공모 id를 통해 삭제된 제목이라고 명시된 채팅방 상세 조회를 할 수 있다")
        @Test
        void should_getDeletedCommentRoomInfo_when_givenDeletedOfferingId() {
            // given
            offeringFixture.deleteOffering(offering);

            // when
            CommentRoomInfoResponse response = commentService.getCommentRoomInfo(offering.getId(), member);

            // then
            assertEquals(response.status(), CommentRoomStatus.DELETED);
        }
    }
}
