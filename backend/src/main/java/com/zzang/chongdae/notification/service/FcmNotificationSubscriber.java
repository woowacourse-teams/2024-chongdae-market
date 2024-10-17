package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FcmNotificationSubscriber {

    public TopicManagementResponse subscribe(MemberEntity member, String topic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .subscribeToTopic(List.of(member.getFcmToken()), topic);
            log.info("구독 성공 개수: {}", response.getSuccessCount());
            log.info("구독 실패 개수: {}", response.getFailureCount());
            return response;
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public TopicManagementResponse unsubscribe(MemberEntity member, String topic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(List.of(member.getFcmToken()), topic);
            log.info("구독 취소 성공 개수: {}", response.getSuccessCount());
            log.info("구독 취소 실패 개수: {}", response.getFailureCount());
            return response;
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
