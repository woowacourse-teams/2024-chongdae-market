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
        FcmToken token = new FcmToken(proposer);
        FcmData data = new FcmData();
        data.addData("title", offering.getTitle());
        data.addData("body", participant.getNickname() + "님이 참여했습니다.");
        data.addData("offering_id", offering.getId());
        data.addData("type", "comment_room");
        return messageManager.createMessage(token, data);
    }

    public Message messageWhenCancelParticipate() {
        FcmToken token = new FcmToken(proposer);
        FcmData data = new FcmData();
        data.addData("title", offering.getTitle());
        data.addData("body", participant.getNickname() + "님이 참여를 취소했습니다.");
        data.addData("offering_id", offering.getId());
        data.addData("type", "comment_room");
        return messageManager.createMessage(token, data);
    }
}
