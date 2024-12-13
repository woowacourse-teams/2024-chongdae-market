package com.zzang.chongdae.offeringmember.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.service.dto.ParticipantResponse;
import com.zzang.chongdae.offeringmember.service.dto.ParticipationRequest;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OfferingMemberServiceTest extends ServiceTest {

    @Autowired
    private OfferingMemberService offeringMemberService;

    @DisplayName("동시에 참여할 경우 중복 참여가 가능하다.")
    @Test
    void should_participateInDuplicate() throws InterruptedException {
        // given
        MemberEntity proposer = memberFixture.createMember("ever");
        OfferingEntity offering = offeringFixture.createOffering(proposer);
        offeringMemberFixture.createProposer(proposer, offering);

        // when
        ParticipationRequest request = new ParticipationRequest(offering.getId());
        MemberEntity participant = memberFixture.createMember("whoever");

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        int executeCount = 2;
        CountDownLatch countDownLatch = new CountDownLatch(executeCount);

        for (int i = 0; i < executeCount; i++) {
            executorService.execute(() -> {
                offeringMemberService.participate(request, participant);
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        // then
        ParticipantResponse response = offeringMemberService.getAllParticipant(offering.getId(), proposer);
        assertThat(response.participants()).hasSize(executeCount);
    }
}
