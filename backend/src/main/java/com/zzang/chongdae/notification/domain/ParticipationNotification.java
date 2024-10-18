package com.zzang.chongdae.notification.domain;

import com.google.firebase.messaging.Message;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.service.FcmMessageManager;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;

public class ParticipationNotification {

    private final FcmMessageManager messageManager;
    private final OfferingEntity offering;
    private final MemberEntity proposer;
    private final MemberEntity participant;

    public ParticipationNotification(FcmMessageManager messageManager, OfferingMemberEntity offeringMember) {
        this.messageManager = messageManager;
        this.offering = offeringMember.getOffering();
        this.proposer = offering.getMember();
        this.participant = offeringMember.getMember();
    }

    public Message messageWhenParticipate() {
        return messageManager.createMessage(
                new FcmToken(proposer.getFcmToken()),
                offering.getTitle(),
                participant.getNickname() + "이(가) 참여했습니다.");
    }

    public Message messageWhenCancelParticipate() {
        return messageManager.createMessage(
                new FcmToken(proposer.getFcmToken()),
                offering.getTitle(),
                participant.getNickname() + "이(가) 참여를 취소하였습니다.");
    }
}
