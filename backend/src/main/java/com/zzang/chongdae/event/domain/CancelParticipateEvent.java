package com.zzang.chongdae.event.domain;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.domain.FcmToken;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CancelParticipateEvent extends ApplicationEvent {

    private final OfferingMemberEntity offeringMember;
    private final MemberEntity participant;
    private final FcmToken token;

    public CancelParticipateEvent(Object source, OfferingMemberEntity offeringMember) {
        super(source);
        this.offeringMember = offeringMember;
        this.participant = offeringMember.getMember();
        this.token = createFcmToken();
    }

    private FcmToken createFcmToken() {
        return new FcmToken(offeringMember.getMember());
    }
}
