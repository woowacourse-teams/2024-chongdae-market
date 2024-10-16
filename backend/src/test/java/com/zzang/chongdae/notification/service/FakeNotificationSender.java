package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakeNotificationSender implements NotificationSender {

    private static final Logger log = LoggerFactory.getLogger(FakeNotificationSender.class);

    @Override
    public String send(Message message) {
        String response = "fakeMessageId";
        log.info("알림 메시지 전송 성공: {}", response);
        return response;
    }
}
