package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.Message;
import com.zzang.chongdae.notification.domain.ParticipationNotification;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FcmNotificationService {

    private final NotificationSender notificationSender;

    public String sendParticipation(OfferingMemberEntity offeringMember) {
        ParticipationNotification participationNotification = new ParticipationNotification(offeringMember);
        Message message = participationNotification.messageWhenParticipate();
        return notificationSender.send(message);
    }

    public String sendCancelParticipation(OfferingMemberEntity offeringMember) {
        ParticipationNotification participationNotification = new ParticipationNotification(offeringMember);
        Message message = participationNotification.messageWhenCancelParticipate();
        return notificationSender.send(message);
    }
}
