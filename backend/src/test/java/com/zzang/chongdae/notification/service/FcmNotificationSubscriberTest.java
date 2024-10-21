package com.zzang.chongdae.notification.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.firebase.messaging.TopicManagementResponse;
import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.domain.FcmTopic;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FcmNotificationSubscriberTest extends ServiceTest {

    @DisplayName("유효하지 않은 토큰을 가진 사용자에 대해 주제를 구독할 경우 구독 성공 횟수 0, 실패 횟수 1이다.")
    @Test
    void should_getCount_when_successToSubscribe() {
        // given
        MemberEntity member = memberFixture.createMember("ever", "invalidFcmToken");
        FcmNotificationSubscriber subscriber = new FcmNotificationSubscriber();

        // when
        TopicManagementResponse response = subscriber.subscribe(member, FcmTopic.memberTopic());

        // then
        assertThat(response.getSuccessCount()).isEqualTo(0);
        assertThat(response.getFailureCount()).isEqualTo(1);
    }

    @Disabled
    @DisplayName("유효한 토큰을 가진 사용자에 대해 주제를 구독할 경우 구독 성공 횟수 1, 실패 횟수 0이다.")
    @Test
    void should_getCount_when_failToSubscribe() {
        // given
        MemberEntity member = memberFixture.createMember("ever", "youHaveToChangeThis");
        FcmNotificationSubscriber subscriber = new FcmNotificationSubscriber();

        // when
        TopicManagementResponse response = subscriber.subscribe(member, FcmTopic.memberTopic());

        // then
        assertThat(response.getSuccessCount()).isEqualTo(1);
        assertThat(response.getFailureCount()).isEqualTo(0);
    }
}
