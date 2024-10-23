package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.domain.FcmTopic;
import com.zzang.chongdae.notification.exception.NotificationErrorCode;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FcmNotificationSubscriber implements NotificationSubscriber {

    private static final String ERROR_MESSAGE_WHEN_INVALID_TOKEN = "The registration token is not a valid FCM registration token";
    private static final String ERROR_MESSAGE_WHEN_OLD_TOKEN = "Requested entity was not found";
    private static final String ERROR_MESSAGE_WHEN_TOKEN_EMPTY = "Exactly one of token, topic or condition must be specified";
    private static final String ERROR_MESSAGE_WHEN_NULL_TOKEN = "registration tokens list must not contain null or empty strings";

    @Override
    public TopicManagementResponse subscribe(MemberEntity member, FcmTopic topic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .subscribeToTopic(List.of(member.getFcmToken()), topic.getValue());
            log.info("구독 성공 개수: {}", response.getSuccessCount());
            log.info("구독 실패 개수: {}", response.getFailureCount());
            return response;
        } catch (FirebaseMessagingException e) {
            return subscribeWhenFail(e);
        }
    }

    @Override
    public TopicManagementResponse unsubscribe(MemberEntity member, FcmTopic topic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(List.of(member.getFcmToken()), topic.getValue());
            log.info("구독 취소 성공 개수: {}", response.getSuccessCount());
            log.info("구독 취소 실패 개수: {}", response.getFailureCount());
            return response;
        } catch (FirebaseMessagingException e) {
            return subscribeWhenFail(e);
        }
    }

    private TopicManagementResponse subscribeWhenFail(FirebaseMessagingException e) {
        if (isInvalidToken(e)) {
            log.error("토픽 구독 실패: {}", "유효하지 않은 토큰");
            return null;
        }
        log.error("토픽 구독 실패: {}", e.getMessage());
        e.printStackTrace();
        throw new MarketException(NotificationErrorCode.CANNOT_SEND_ALARM);
    }

    private boolean isInvalidToken(FirebaseMessagingException e) {
        return e.getMessage().contains(ERROR_MESSAGE_WHEN_INVALID_TOKEN)
                || e.getMessage().contains(ERROR_MESSAGE_WHEN_OLD_TOKEN)
                || e.getMessage().contains(ERROR_MESSAGE_WHEN_TOKEN_EMPTY)
                || e.getMessage().contains(ERROR_MESSAGE_WHEN_NULL_TOKEN);
    }
}
