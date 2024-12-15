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
        ParticipationRequest request = new ParticipationRequest(offering.getId());
        MemberEntity participant = memberFixture.createMember("whoever");

        int executeCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(executeCount);
        CountDownLatch countDownLatch = new CountDownLatch(executeCount);

        for (int i = 0; i < executeCount; i++) {
            executorService.submit(() -> {
                try {
                    offeringMemberService.participate(request, participant);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

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
        ParticipationRequest request = new ParticipationRequest(offering.getId());
        MemberEntity participant1 = memberFixture.createMember("ever1");
        MemberEntity participant2 = memberFixture.createMember("ever2");

        int executeCount = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(executeCount);
        CountDownLatch countDownLatch = new CountDownLatch(executeCount);

        executorService.submit(() -> {
            try {
                offeringMemberService.participate(request, participant1);
            } finally {
                countDownLatch.countDown();
            }
        });
        executorService.submit(() -> {
            try {
                offeringMemberService.participate(request, participant2);
            } finally {
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
        executorService.shutdown();

        // then
        ParticipantResponse response = offeringMemberService.getAllParticipant(offering.getId(), proposer);
        assertThat(response.participants()).hasSize(1);
    }
}
