package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.exception.NotificationErrorCode;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FcmNotificationService {

    public String sendParticipation(OfferingEntity offering, MemberEntity participant) {
        MemberEntity proposer = offering.getMember();
        Message message = Message.builder()
                .setToken(proposer.getFcmToken())
                .setNotification(notification(offering.getTitle(), participant.getNickname() + "이(가) 참여했습니다."))
                .build();
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

    private Notification notification(String title, String body) {
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }
}
