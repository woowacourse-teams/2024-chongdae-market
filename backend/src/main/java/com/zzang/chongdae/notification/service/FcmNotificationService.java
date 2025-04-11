package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.domain.FcmToken;
import com.zzang.chongdae.notification.domain.FcmTokens;
import com.zzang.chongdae.notification.domain.FcmTopic;
import com.zzang.chongdae.notification.service.message.CommentMessageManager;
import com.zzang.chongdae.notification.service.message.OfferingMessageManager;
import com.zzang.chongdae.notification.service.message.ParticipationMessageManager;
import com.zzang.chongdae.notification.service.message.RoomStatusMessageManager;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FcmNotificationService {

    private final NotificationSender notificationSender;
    private final NotificationSubscriber notificationSubscriber;
    private final CommentMessageManager commentMessageManager;
    private final OfferingMessageManager offeringMessageManager;
    private final ParticipationMessageManager participationMessageManager;
    private final RoomStatusMessageManager roomStatusMessageManager; // TODO: 의존성 리팩터링

    public void participate(OfferingMemberEntity offeringMember, FcmToken token) {
        long start = System.currentTimeMillis();
        FcmTopic topic = FcmTopic.participantTopic(offeringMember.getOffering());
        notificationSubscriber.subscribe(offeringMember.getMember(), topic);
        Message message = participationMessageManager.messageWhenParticipate(offeringMember, token);
        notificationSender.send(message);
        long end = System.currentTimeMillis();
        log.info("참여하기 푸시알림 관련 처리 시간: {}ms", end - start);
    }

    public void cancelParticipation(OfferingMemberEntity offeringMember, MemberEntity participant, FcmToken token) {
        FcmTopic topic = FcmTopic.participantTopic(offeringMember.getOffering());
        notificationSubscriber.unsubscribe(offeringMember.getMember(), topic);
        Message message = participationMessageManager.messageWhenCancelParticipate(offeringMember, participant, token);
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
    public BatchResponse saveComment(CommentEntity comment, FcmTokens tokens) {
        if (tokens.isEmpty()) {
            return null;
        }
        MulticastMessage message = commentMessageManager.messageWhenSaveComment(comment, tokens);
        return notificationSender.send(message);
    }

    public void login(MemberEntity member) {
        FcmTopic topic = FcmTopic.memberTopic();
        notificationSubscriber.subscribe(member, topic);
    }
}
