package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.SendResponse;
import java.util.List;
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

    @Override
    public BatchResponse send(MulticastMessage message) {
        FakeBatchResponse response = new FakeBatchResponse();
        log.info("알림 메시지 전송 성공 개수: {}", response.getSuccessCount());
        log.info("알림 메시지 전송 실패 개수: {}", response.getFailureCount());
        return response;
    }

    private static class FakeBatchResponse implements BatchResponse {

        @Override
        public List<SendResponse> getResponses() {
            return List.of();
        }

        @Override
        public int getSuccessCount() {
            return 1;
        }

        @Override
        public int getFailureCount() {
            return 0;
        }
    }
}
