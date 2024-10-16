package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.notification.exception.NotificationErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FcmNotificationSender implements NotificationSender {

    @Override
    public String send(Message message) {
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("알림 메시지 전송 성공: {}", response);
            return response;
        } catch (FirebaseMessagingException e) {
            log.error("알림 메시지 전송 실패: {}", e.getMessage());
            e.printStackTrace();
            throw new MarketException(NotificationErrorCode.CANNOT_SEND_ALARM);
        }
    }
}
