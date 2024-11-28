package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.TopicManagementResponse;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.domain.FcmTopic;
import com.zzang.chongdae.notification.exception.NotificationException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FcmNotificationSubscriber implements NotificationSubscriber {

    @Override
    public TopicManagementResponse subscribe(MemberEntity member, FcmTopic topic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .subscribeToTopic(List.of(member.getFcmToken()), topic.getValue());
            log.info("구독 성공 {}개, 구독 실패 {}개", response.getSuccessCount(), response.getFailureCount());
            return response;
        } catch (Exception e) {
            throw new NotificationException(e);
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
            throw new NotificationException(e);
        }
    }
}
