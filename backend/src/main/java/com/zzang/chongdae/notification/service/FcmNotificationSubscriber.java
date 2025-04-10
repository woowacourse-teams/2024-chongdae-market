package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.domain.FcmTopic;
import com.zzang.chongdae.notification.exception.NotificationException;
import com.zzang.chongdae.notification.exception.NotificationRetryableException;
import java.util.List;
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
public class FcmNotificationSubscriber implements NotificationSubscriber {

    private static final int FCM_SERVER_ERROR_CODE = 500;

    @Override
    public TopicManagementResponse subscribe(MemberEntity member, FcmTopic topic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .subscribeToTopic(List.of(member.getFcmToken()), topic.getValue());
            log.info("구독 성공 {}개, 구독 실패 {}개", response.getSuccessCount(), response.getFailureCount());
            return response;
        } catch (Exception e) {
            throw decideException(e);
        }
    }

    @Override
    public TopicManagementResponse unsubscribe(MemberEntity member, FcmTopic topic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(List.of(member.getFcmToken()), topic.getValue());
            log.info("구독취소 성공 {}개, 구독취소 실패 {}개", response.getSuccessCount(), response.getFailureCount());
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
