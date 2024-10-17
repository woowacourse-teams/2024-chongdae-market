package com.zzang.chongdae.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.firebase.messaging.Message;
import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoomStatusNotificationTest extends ServiceTest {

    @DisplayName("FCM에 전송할 메시지를 생성한다.")
    @Test
    void should_notNull_when_createMessage() {
        // given
        MemberEntity proposer = memberFixture.createMember("ever");
        OfferingEntity offering = offeringFixture.createOffering(proposer);
        RoomStatusNotification notification = new RoomStatusNotification(offering);

        // when
        Message message = notification.messageWhenUpdateStatus();

        // then
        assertThat(message).isNotNull();
    }
}
