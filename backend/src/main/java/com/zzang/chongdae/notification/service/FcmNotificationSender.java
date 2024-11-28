package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.zzang.chongdae.notification.exception.NotificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FcmNotificationSender implements NotificationSender {

    @Override
    public String send(Message message) {
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("전송 성공: {}", response);
            return response;
        } catch (Exception e) {
            throw new NotificationException(e);
        }
    }

    @Override
    public BatchResponse send(MulticastMessage message) {
        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
            log.info("전송 성공 {}개, 전송 실패 {}개", response.getSuccessCount(), response.getFailureCount());
            return response;
        } catch (Exception e) {
            throw new NotificationException(e);
        }
    }
}
