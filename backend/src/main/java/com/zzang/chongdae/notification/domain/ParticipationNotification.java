package com.zzang.chongdae.notification.domain;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;

public class ParticipationNotification {

    private final OfferingEntity offering;
    private final MemberEntity proposer;
    private final MemberEntity participant;

    public ParticipationNotification(OfferingMemberEntity offeringMember) {
        this.offering = offeringMember.getOffering();
        this.proposer = offering.getMember();
        this.participant = offeringMember.getMember();
    }

    public Message messageWhenParticipate() {
        return Message.builder()
                .setToken(proposer.getFcmToken())
                .setNotification(notification(offering.getTitle(), participant.getNickname() + "이(가) 참여했습니다."))
                .build();
    }

    public Message messageWhenCancelParticipate() {
        return Message.builder()
                .setToken(proposer.getFcmToken())
                .setNotification(notification(offering.getTitle(), participant.getNickname() + "이(가) 참여를 취소하였습니다."))
                .build();
    }

    private Notification notification(String title, String body) {
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }
}
