package com.zzang.chongdae.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TestEventPublisher {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    public void publishWithoutTransaction(ApplicationEvent event) {
        eventPublisher.publishEvent(event);
    }

    @Transactional
    public void publishWithTransaction(ApplicationEvent event) {
        eventPublisher.publishEvent(event);
    }

    @Transactional
    public void publishWithTransactionThenThrowException(ApplicationEvent event) {
        eventPublisher.publishEvent(event);
        throw new RuntimeException();
    }
}
