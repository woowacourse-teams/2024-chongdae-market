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
    private static final String ERROR_MESSAGE_WHEN_OLD_TOKEN = "Requested entity was not found";
    private static final String ERROR_MESSAGE_WHEN_TOKEN_EMPTY = "Exactly one of token, topic or condition must be specified";
    private static final String ERROR_MESSAGE_WHEN_NULL_TOKEN = "registration tokens list must not contain null or empty strings";

    @Override
    public String send(Message message) {
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("알림 메시지 전송 성공: {}", response);
            return response;
        } catch (FirebaseMessagingException e) {
            return sendWhenFail(e);
        }
    }

    @Override
    public BatchResponse send(MulticastMessage message) {
        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
            log.info("알림 메시지 전송 성공 개수: {}", response.getSuccessCount());
            log.info("알림 메시지 전송 실패 개수: {}", response.getFailureCount());
            return response;
        } catch (FirebaseMessagingException | IllegalArgumentException e) {
            return sendWhenFailWithBatch(e);
        }
    }

    private String sendWhenFail(Exception e) {
        if (isInvalidToken(e)) {
            log.error("알림 메시지 전송 실패: {}", "유효하지 않은 토큰");
            return "";
        }
        log.error("알림 메시지 전송 실패: {}", e.getMessage());
        e.printStackTrace();
        throw new MarketException(NotificationErrorCode.CANNOT_SEND_ALARM);
    }

    private BatchResponse sendWhenFailWithBatch(Exception e) {
        if (isInvalidToken(e)) {
            log.error("알림 메시지 전송 실패: {}", "유효하지 않은 토큰");
            return null;
        }
        log.error("알림 메시지 전송 실패: {}", e.getMessage());
        e.printStackTrace();
        throw new MarketException(NotificationErrorCode.CANNOT_SEND_ALARM);
    }

    private boolean isInvalidToken(Exception e) {
        return e.getMessage().contains(ERROR_MESSAGE_WHEN_INVALID_TOKEN)
                || e.getMessage().contains(ERROR_MESSAGE_WHEN_OLD_TOKEN)
                || e.getMessage().contains(ERROR_MESSAGE_WHEN_TOKEN_EMPTY)
                || e.getMessage().contains(ERROR_MESSAGE_WHEN_NULL_TOKEN);
    }
}
