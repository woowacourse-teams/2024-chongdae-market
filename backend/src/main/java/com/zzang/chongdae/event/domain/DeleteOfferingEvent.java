package com.zzang.chongdae.event.domain;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteOfferingEvent extends ApplicationEvent {

    private final OfferingEntity offering;

    public DeleteOfferingEvent(Object source, OfferingEntity offering) {
        super(source);
        this.offering = offering;
    }
}
