package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.notification.exception.NotificationErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FcmNotificationSender implements NotificationSender {

    private static final String ERROR_MESSAGE_WHEN_INVALID_TOKEN = "The registration token is not a valid FCM registration token";

    @Override
    public String send(Message message) {
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("알림 메시지 전송 성공: {}", response);
            return response;
        } catch (FirebaseMessagingException e) {
            log.error("알림 메시지 전송 실패: {}", e.getMessage());
            return sendWhenFail(e);
        }
    }

    private String sendWhenFail(FirebaseMessagingException e) {
        if (isInvalidToken(e)) {
            return "";
        }
        e.printStackTrace();
        throw new MarketException(NotificationErrorCode.CANNOT_SEND_ALARM);
    }

    private boolean isInvalidToken(FirebaseMessagingException e) {
        return e.getMessage().equals(ERROR_MESSAGE_WHEN_INVALID_TOKEN);
    }

    @Override
    public BatchResponse send(MulticastMessage message) {
        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
            log.info("알림 메시지 전송 성공 개수: {}", response.getSuccessCount());
            log.info("알림 메시지 전송 실패 개수: {}", response.getFailureCount());
            return response;
        } catch (FirebaseMessagingException e) {
            log.error("알림 메시지 전송 실패: {}", e.getMessage());
            e.printStackTrace();
            throw new MarketException(NotificationErrorCode.CANNOT_SEND_ALARM);
        }
    }
}
