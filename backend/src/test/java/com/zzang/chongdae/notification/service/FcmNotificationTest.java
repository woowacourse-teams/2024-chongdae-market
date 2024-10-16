package com.zzang.chongdae.notification.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FcmNotificationTest extends ServiceTest {

    @Autowired
    private FcmNotificationService alarmService;

    @Disabled
    @DisplayName("FCM에 메시지를 전송할 수 있다.")
    @Test
    void should_sendAlarmToFcm() {
        // given
        String proposerFcmToken = "youHaveToChangeThis1";
        String participantFcmToken = "youHaveToChangeThis2";
        MemberEntity proposer = memberFixture.createMember("proposer", proposerFcmToken);
        MemberEntity participant = memberFixture.createMember("ever", participantFcmToken);
        OfferingEntity offering = offeringFixture.createOffering(proposer);
        offeringMemberFixture.createProposer(proposer, offering);
        OfferingMemberEntity offeringMember = offeringMemberFixture.createParticipant(participant, offering);

        // when
        String messageId = alarmService.sendParticipation(offeringMember);

        // then
        assertThat(messageId).contains("messages");
    }
}
