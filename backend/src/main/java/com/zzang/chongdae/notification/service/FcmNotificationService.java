package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.domain.FcmTopic;
import com.zzang.chongdae.notification.service.message.CommentMessageManager;
import com.zzang.chongdae.notification.service.message.OfferingMessageManager;
import com.zzang.chongdae.notification.service.message.ParticipationMessageManager;
import com.zzang.chongdae.notification.service.message.RoomStatusMessageManager;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import java.util.List;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FcmNotificationService {

    private final NotificationSender notificationSender;
    private final NotificationSubscriber notificationSubscriber;
    private final CommentMessageManager commentMessageManager;
    private final OfferingMessageManager offeringMessageManager;
    private final ParticipationMessageManager participationMessageManager;
    private final RoomStatusMessageManager roomStatusMessageManager; // TODO: 의존성 리팩터링

    public void participate(OfferingMemberEntity offeringMember) {
        FcmTopic topic = FcmTopic.participantTopic(offeringMember.getOffering());
        notificationSubscriber.subscribe(offeringMember.getMember(), topic);
        Message message = participationMessageManager.messageWhenParticipate(offeringMember);
        notificationSender.send(message);
    }

    public void cancelParticipation(OfferingMemberEntity offeringMember) {
        FcmTopic topic = FcmTopic.participantTopic(offeringMember.getOffering());
        notificationSubscriber.unsubscribe(offeringMember.getMember(), topic);
        Message message = participationMessageManager.messageWhenCancelParticipate(offeringMember);
        notificationSender.send(message);
    }

    public void updateStatus(OfferingEntity offering) {
        Message message = roomStatusMessageManager.messageWhenUpdateStatus(offering);
        notificationSender.send(message);
    }

    public void saveOffering(OfferingEntity offering) {
        FcmTopic topic = FcmTopic.proposerTopic(offering);
        notificationSubscriber.subscribe(offering.getMember(), topic);
        Message message = offeringMessageManager.messageWhenSaveOffering(offering);
        notificationSender.send(message);
    }

    public void deleteOffering(OfferingEntity offering) {
        FcmTopic topic = FcmTopic.proposerTopic(offering);
        notificationSubscriber.unsubscribe(offering.getMember(), topic);
    }

    @Nullable
    public BatchResponse saveComment(CommentEntity comment,
                                     List<OfferingMemberEntity> offeringMembers) { // todo: 참여자 도메인 추출
        MulticastMessage message = commentMessageManager.messageWhenSaveComment(comment, offeringMembers);
        if (message == null) {
            return null;
        }
        return notificationSender.send(message);
    }

    public void login(MemberEntity member) {
        FcmTopic topic = FcmTopic.memberTopic();
        notificationSubscriber.subscribe(member, topic);
    }
}
