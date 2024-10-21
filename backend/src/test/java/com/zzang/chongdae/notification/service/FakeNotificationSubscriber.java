package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.TopicManagementResponse;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakeNotificationSubscriber implements NotificationSubscriber {

    private static final Logger log = LoggerFactory.getLogger(FakeNotificationSubscriber.class);

    @Override
    public TopicManagementResponse subscribe(MemberEntity member, String topic) {
        log.info("구독 성공 개수: {}", 1);
        log.info("구독 실패 개수: {}", 0);
        return null;
    }

    @Override
    public TopicManagementResponse unsubscribe(MemberEntity member, String topic) {
        log.info("구독 취소 성공 개수: {}", 1);
        log.info("구독 취소 실패 개수: {}", 0);
        return null;
    }
}