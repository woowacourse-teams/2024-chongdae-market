package com.zzang.chongdae.offeringmember.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import com.zzang.chongdae.comment.service.CommentService;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.global.helper.ConcurrencyExecutor;
import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offering.service.OfferingService;
import com.zzang.chongdae.offeringmember.service.dto.ParticipantResponse;
import com.zzang.chongdae.offeringmember.service.dto.ParticipationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OfferingMemberServiceConcurrencyTest extends ServiceTest {

    @Autowired
    OfferingMemberService offeringMemberService;

    @Autowired
    CommentService commentService;

    @Autowired
    OfferingService offeringService;

    @Autowired
    ConcurrencyExecutor concurrencyExecutor;

    @DisplayName("같은 사용자가 같은 공모에 동시에 참여할 경우 중복 참여가 불가능하다.")
    @Test
    void should_failParticipate_when_givenSameMemberAndSameOffering() throws InterruptedException {
        // given
        MemberEntity proposer = memberFixture.createMember("ever");
        OfferingEntity offering = offeringFixture.createOffering(proposer);
        offeringMemberFixture.createProposer(proposer, offering);

        // when
        ParticipationRequest request = new ParticipationRequest(offering.getId(), 1);
        MemberEntity participant = memberFixture.createMember("whoever");

        concurrencyExecutor.executeWithoutResult(() -> offeringMemberService.participate(request, participant), 5);

        // then
        ParticipantResponse response = offeringMemberService.getAllParticipant(offering.getId(), proposer);
        assertThat(response.participants()).hasSize(1);
    }

    @DisplayName("다른 사용자가 같은 공모에 동시에 참여할 경우 중복 참여가 불가능하다.")
    @Test
    void should_failParticipate_when_givenDifferentMemberAndSameOffering() throws InterruptedException {
        // given
        MemberEntity proposer = memberFixture.createMember("ever");
        OfferingEntity offering = offeringFixture.createOffering(proposer, 2);
        offeringMemberFixture.createProposer(proposer, offering);

        // when
        ParticipationRequest request = new ParticipationRequest(offering.getId(), 1);
        MemberEntity participant1 = memberFixture.createMember("ever1");
        MemberEntity participant2 = memberFixture.createMember("ever2");

        concurrencyExecutor.executeWithoutResult(
                () -> offeringMemberService.participate(request, participant1),
                () -> offeringMemberService.participate(request, participant2));

        // then
        ParticipantResponse response = offeringMemberService.getAllParticipant(offering.getId(), proposer);
        assertThat(response.participants()).hasSize(1);
    }

    @DisplayName("참여자 나가기 테스트")
    @Nested
    class CancelParticipateWithParticipant {

        @DisplayName("진행중인 공모 중에 참여자는 나갈 수 없다.")
        @Test
        void should_participantFailCancelParticipate_when_offeringIsNotGrouping() throws InterruptedException {
            // given
            MemberEntity proposer = memberFixture.createMember("poke");
            OfferingEntity offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);

            // when
            ParticipationRequest request = new ParticipationRequest(offering.getId(), 1);
            MemberEntity participant = memberFixture.createMember("강서총각");

            offeringMemberService.participate(request, participant);
            commentService.updateCommentRoomStatus(offering.getId(), proposer);

            // then
            assertThatThrownBy(() -> offeringMemberService.cancelParticipate(offering.getId(), participant))
                    .isInstanceOf(MarketException.class);
        }

        @DisplayName("거래가 완료된 공모일 경우 참여자는 나갈 수 있다.")
        @Test
        void should_participantCancelParticipate_when_offeringIsDone() throws InterruptedException {
            // given
            MemberEntity proposer = memberFixture.createMember("poke");
            OfferingEntity offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);

            // when
            ParticipationRequest request = new ParticipationRequest(offering.getId(), 1);
            MemberEntity participant = memberFixture.createMember("강서총각");

            offeringMemberService.participate(request, participant);
            commentService.updateCommentRoomStatus(offering.getId(), proposer); // Buying
            commentService.updateCommentRoomStatus(offering.getId(), proposer); // Trading
            commentService.updateCommentRoomStatus(offering.getId(), proposer); // Done

            // then
            assertThatCode(() -> offeringMemberService.cancelParticipate(offering.getId(), participant))
                    .doesNotThrowAnyException();
        }

        @DisplayName("삭제된 공모일 경우 총대는 나갈 수 있다.")
        @Test
        void should_participantCancelParticipate_when_offeringIsDeleted() throws InterruptedException {
            // given
            MemberEntity proposer = memberFixture.createMember("poke");
            OfferingEntity offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);

            // when
            ParticipationRequest request = new ParticipationRequest(offering.getId(), 1);
            MemberEntity participant = memberFixture.createMember("강서총각");

            offeringMemberService.participate(request, participant);
            offeringService.deleteOffering(offering.getId(), proposer);

            // then
            assertThatCode(() -> offeringMemberService.cancelParticipate(offering.getId(), participant))
                    .doesNotThrowAnyException();
        }

        @DisplayName("모집중에 총대는 나갈 수 없다.")
        @Test
        void should_proposerFailCancelParticipate_when_offeringIsNotGrouping() throws InterruptedException {
            // given
            MemberEntity proposer = memberFixture.createMember("poke");
            OfferingEntity offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);

            // when
            ParticipationRequest request = new ParticipationRequest(offering.getId(), 1);
            MemberEntity participant = memberFixture.createMember("강서총각");

            offeringMemberService.participate(request, participant);

            // then
            assertThatThrownBy(() -> offeringMemberService.cancelParticipate(offering.getId(), proposer))
                    .isInstanceOf(MarketException.class);
        }

        @DisplayName("거래가 완료된 공모일 경우 총대는 나갈 수 있다.")
        @Test
        void should_proposerCancelParticipate_when_offeringIsDone() throws InterruptedException {
            // given
            MemberEntity proposer = memberFixture.createMember("poke");
            OfferingEntity offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);

            // when
            ParticipationRequest request = new ParticipationRequest(offering.getId(), 1);
            MemberEntity participant = memberFixture.createMember("강서총각");

            offeringMemberService.participate(request, participant);
            commentService.updateCommentRoomStatus(offering.getId(), proposer); // Buying
            commentService.updateCommentRoomStatus(offering.getId(), proposer); // Trading
            commentService.updateCommentRoomStatus(offering.getId(), proposer); // Done

            // then
            assertThatCode(() -> offeringMemberService.cancelParticipate(offering.getId(), participant))
                    .doesNotThrowAnyException();
        }

        @DisplayName("삭제된 공모일 경우 총대는 나갈 수 있다.")
        @Test
        void should_proposerCancelParticipate_when_offeringIsDeleted() throws InterruptedException {
            // given
            MemberEntity proposer = memberFixture.createMember("poke");
            OfferingEntity offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);

            // when
            ParticipationRequest request = new ParticipationRequest(offering.getId(), 1);
            MemberEntity participant = memberFixture.createMember("강서총각");

            offeringMemberService.participate(request, participant);
            offeringService.deleteOffering(offering.getId(), proposer);

            // then
            assertThatCode(() -> offeringMemberService.cancelParticipate(offering.getId(), proposer))
                    .doesNotThrowAnyException();
        }
    }
}
