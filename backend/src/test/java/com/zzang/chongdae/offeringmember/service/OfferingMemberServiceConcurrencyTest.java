package com.zzang.chongdae.offeringmember.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.zzang.chongdae.global.helper.ConcurrencyExecutor;
import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.service.dto.ParticipantResponse;
import com.zzang.chongdae.offeringmember.service.dto.ParticipationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OfferingMemberServiceConcurrencyTest extends ServiceTest {

    @Autowired
    private OfferingMemberService offeringMemberService;

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

        ConcurrencyExecutor concurrencyExecutor = ConcurrencyExecutor.getInstance();
        concurrencyExecutor.executeWithoutResult(5,
                () -> offeringMemberService.participate(request, participant));

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

        ConcurrencyExecutor concurrencyExecutor = ConcurrencyExecutor.getInstance();
        concurrencyExecutor.executeWithoutResult(
                () -> offeringMemberService.participate(request, participant1),
                () -> offeringMemberService.participate(request, participant2));

        // then
        ParticipantResponse response = offeringMemberService.getAllParticipant(offering.getId(), proposer);
        assertThat(response.participants()).hasSize(1);
    }
}
