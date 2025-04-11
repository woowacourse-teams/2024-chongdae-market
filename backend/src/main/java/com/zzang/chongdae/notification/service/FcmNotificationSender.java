package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.zzang.chongdae.notification.exception.NotificationException;
import com.zzang.chongdae.notification.exception.NotificationRetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Retryable(
        retryFor = NotificationRetryableException.class,
        backoff = @Backoff(delay = 10_000, multiplier = 2, random = true)
)
@Component
public class FcmNotificationSender implements NotificationSender {

    private static final int FCM_SERVER_ERROR_CODE = 500;

    @Override
    public String send(Message message) {
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("전송 성공: {}", response);
            return response;
        } catch (Exception e) {
            throw decideException(e);
        }
    }

    @Override
    public BatchResponse send(MulticastMessage message) {
        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
            log.info("전송 성공 {}개, 전송 실패 {}개", response.getSuccessCount(), response.getFailureCount());
            return response;
        } catch (Exception e) {
            throw decideException(e);
        }
    }

    private RuntimeException decideException(Exception e) {
        if (isRetryable(e)) {
            return new NotificationRetryableException(e);
        }
        return new NotificationException(e);
    }

    private boolean isRetryable(Exception ex) {
        return ex instanceof FirebaseMessagingException firebaseEx
                && firebaseEx.getHttpResponse().getStatusCode() >= FCM_SERVER_ERROR_CODE;
    }
}
