package com.zzang.chongdae.notification.service.message;

import com.google.firebase.messaging.Message;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.domain.FcmData;
import com.zzang.chongdae.notification.domain.FcmToken;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ParticipationMessageManager {

    private static final String MESSAGE_BODY_FORMAT_PARTICIPATE = "%s 님이 참여했습니다.";
    private static final String MESSAGE_BODY_FORMAT_CANCEL = "%s 님이 참여를 취소했습니다.";
    private static final String MESSAGE_TYPE = "comment_detail";

    private final FcmMessageCreator messageCreator;

    public Message messageWhenParticipate(OfferingMemberEntity offeringMember) {
        OfferingEntity offering = offeringMember.getOffering();
        MemberEntity proposer = offering.getMember();
        MemberEntity participant = offeringMember.getMember();

        FcmToken token = new FcmToken(proposer);
        FcmData data = new FcmData();
        data.addData("title", offering.getTitle());
        data.addData("body", MESSAGE_BODY_FORMAT_PARTICIPATE.formatted(participant.getNickname()));
        data.addData("offering_id", offering.getId());
        data.addData("type", MESSAGE_TYPE);
        return messageCreator.createMessage(token, data);
    }

    public Message messageWhenCancelParticipate(OfferingMemberEntity offeringMember) {
        OfferingEntity offering = offeringMember.getOffering();
        MemberEntity proposer = offering.getMember();
        MemberEntity participant = offeringMember.getMember();

        FcmToken token = new FcmToken(proposer);
        FcmData data = new FcmData();
        data.addData("title", offering.getTitle());
        data.addData("body", MESSAGE_BODY_FORMAT_CANCEL.formatted(participant.getNickname()));
        data.addData("offering_id", offering.getId());
        data.addData("type", MESSAGE_TYPE);
        return messageCreator.createMessage(token, data);
    }
}
