package com.zzang.chongdae.event.domain;

import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ParticipateEvent extends ApplicationEvent {

    private final OfferingMemberEntity offeringMember;

    public ParticipateEvent(Object source, OfferingMemberEntity offeringMember) {
        super(source);
        this.offeringMember = offeringMember;
    }
}
