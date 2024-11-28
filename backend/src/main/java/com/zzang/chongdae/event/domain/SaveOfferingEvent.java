package com.zzang.chongdae.event.domain;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SaveOfferingEvent extends ApplicationEvent {

    private final OfferingEntity offering;

    public SaveOfferingEvent(Object source, OfferingEntity offering) {
        super(source);
        this.offering = offering;
    }
}
