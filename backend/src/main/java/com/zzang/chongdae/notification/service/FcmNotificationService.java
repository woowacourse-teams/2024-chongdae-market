package com.zzang.chongdae.notification.service;

import static com.zzang.chongdae.notification.domain.notification.RoomStatusNotification.TOPIC_FORMAT_OFFERING;
import static com.zzang.chongdae.notification.domain.notification.RoomStatusNotification.TOPIC_FORMAT_OFFERING_PROPOSER;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.domain.notification.CommentNotification;
import com.zzang.chongdae.notification.domain.notification.OfferingNotification;
import com.zzang.chongdae.notification.domain.notification.ParticipationNotification;
import com.zzang.chongdae.notification.domain.notification.RoomStatusNotification;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.repository.OfferingMemberRepository;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import java.util.List;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FcmNotificationService {

    private final FcmMessageManager messageManager;
    private final NotificationSender notificationSender;
    private final NotificationSubscriber notificationSubscriber;
    private final MemberRepository memberRepository;
    private final OfferingMemberRepository offeringMemberRepository;

    public String participate(OfferingMemberEntity offeringMember) { // todo: naming
        ParticipationNotification participationNotification = new ParticipationNotification(messageManager,
                offeringMember);
        Message message = participationNotification.messageWhenParticipate();
        notificationSubscriber.subscribe(offeringMember.getMember(),
                TOPIC_FORMAT_OFFERING.formatted(offeringMember.getOffering().getId()));
        return notificationSender.send(message);
    }

    public String cancelParticipation(OfferingMemberEntity offeringMember) {
        ParticipationNotification participationNotification = new ParticipationNotification(messageManager,
                offeringMember);
        Message message = participationNotification.messageWhenCancelParticipate();
        notificationSubscriber.unsubscribe(offeringMember.getMember(),
                TOPIC_FORMAT_OFFERING.formatted(offeringMember.getOffering().getId()));
        return notificationSender.send(message);
    }

    public String updateStatus(OfferingEntity offering) {
        RoomStatusNotification notification = new RoomStatusNotification(messageManager, offering);
        Message message = notification.messageWhenUpdateStatus();
        return notificationSender.send(message);
    }

    @Nullable
    public BatchResponse saveOffering(OfferingEntity offering) {
        notificationSubscriber.subscribe(offering.getMember(),
                TOPIC_FORMAT_OFFERING_PROPOSER.formatted(offering.getId()));
        List<MemberEntity> allMembers = memberRepository.findAll();
        OfferingNotification notification = new OfferingNotification(messageManager, offering, allMembers);
        MulticastMessage message = notification.messageWhenSaveOffering();
        if (message == null) {
            return null;
        }
        return notificationSender.send(message);
    }

    public void deleteOffering(OfferingEntity offering) {
        notificationSubscriber.unsubscribe(offering.getMember(),
                TOPIC_FORMAT_OFFERING_PROPOSER.formatted(offering.getId()));
    }

    @Nullable
    public BatchResponse saveComment(CommentEntity comment, OfferingEntity offering) { // todo: 참여자 도메인 추출
        List<OfferingMemberEntity> offeringMembers = offeringMemberRepository.findAllByOffering(offering);
        CommentNotification notification = new CommentNotification(messageManager, comment, offeringMembers);
        MulticastMessage message = notification.messageWhenSaveComment();
        if (message == null) {
            return null;
        }
        return notificationSender.send(message);
    }
}
