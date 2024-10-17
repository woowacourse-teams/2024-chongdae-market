package com.zzang.chongdae.notification.service;

import static com.zzang.chongdae.notification.domain.RoomStatusNotification.TOPIC_FORMAT_OFFERING;
import static com.zzang.chongdae.notification.domain.RoomStatusNotification.TOPIC_FORMAT_OFFERING_PROPOSER;

import com.google.firebase.messaging.Message;
import com.zzang.chongdae.notification.domain.ParticipationNotification;
import com.zzang.chongdae.notification.domain.RoomStatusNotification;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FcmNotificationService {

    private final NotificationSender notificationSender;
    private final FcmNotificationSubscriber notificationSubscriber;

    public String participate(OfferingMemberEntity offeringMember) { // todo: naming
        ParticipationNotification participationNotification = new ParticipationNotification(offeringMember);
        Message message = participationNotification.messageWhenParticipate();
        notificationSubscriber.subscribe(offeringMember.getMember(),
                TOPIC_FORMAT_OFFERING.formatted(offeringMember.getOffering().getId()));
        return notificationSender.send(message);
    }

    public String cancelParticipation(OfferingMemberEntity offeringMember) {
        ParticipationNotification participationNotification = new ParticipationNotification(offeringMember);
        Message message = participationNotification.messageWhenCancelParticipate();
        notificationSubscriber.unsubscribe(offeringMember.getMember(),
                TOPIC_FORMAT_OFFERING.formatted(offeringMember.getOffering().getId()));
        return notificationSender.send(message);
    }

    public String updateStatus(OfferingEntity offering) {
        RoomStatusNotification notification = new RoomStatusNotification(offering);
        Message message = notification.messageWhenUpdateStatus();
        return notificationSender.send(message);
    }

    public void saveOffering(OfferingEntity offering) {
        notificationSubscriber.subscribe(offering.getMember(),
                TOPIC_FORMAT_OFFERING_PROPOSER.formatted(offering.getId()));
    }

    public void deleteOffering(OfferingEntity offering) {
        notificationSubscriber.unsubscribe(offering.getMember(),
                TOPIC_FORMAT_OFFERING_PROPOSER.formatted(offering.getId()));
    }
}
