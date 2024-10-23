package com.zzang.chongdae.event.domain;

import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ParticipateCancelEvent extends ApplicationEvent {

    private final OfferingMemberEntity offeringMember;

    public ParticipateCancelEvent(Object source, OfferingMemberEntity offeringMember) {
        super(source);
        this.offeringMember = offeringMember;
    }
}
