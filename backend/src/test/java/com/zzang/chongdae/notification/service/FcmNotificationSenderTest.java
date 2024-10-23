package com.zzang.chongdae.notification.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.domain.FcmData;
import com.zzang.chongdae.notification.domain.FcmToken;
import com.zzang.chongdae.notification.domain.FcmTokens;
import com.zzang.chongdae.notification.service.message.FcmMessageCreator;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FcmNotificationSenderTest extends ServiceTest {

    private final String proposerToken = "youHaveToChangeThis1";
    private final String participant1Token = "youHaveToChangeThis2";
    private final String participant2Token = "youHaveToChangeThis3";

    @Autowired
    private FcmMessageCreator messageCreator;

    @Autowired
    private FcmNotificationSender notificationSender;

    @Disabled
    @DisplayName("FCM에 메시지를 전송할 수 있다.")
    @Test
    void should_sendNotificationToFcm() {
        // given
        MemberEntity proposer = memberFixture.createMember("proposer", proposerToken);
        Message message = messageCreator.createMessage(new FcmToken(proposer), new FcmData());

        // when
        String messageId = notificationSender.send(message);

        // then
        assertThat(messageId).contains("messages");
    }

    @Disabled
    @DisplayName("FCM에 대량 메시지를 전송할 수 있다.")
    @Test
    void should_sendNotificationsToFcm() {
        // given
        MemberEntity participant1 = memberFixture.createMember("ever1", participant1Token);
        MemberEntity participant2 = memberFixture.createMember("ever2", participant2Token);

        FcmTokens tokens = FcmTokens.from(List.of(participant1, participant2));
        MulticastMessage messages = messageCreator.createMessages(tokens, new FcmData());

        // when
        BatchResponse response = notificationSender.send(messages);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getSuccessCount()).isEqualTo(2);
    }
}
