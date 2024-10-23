package com.zzang.chongdae.event.service;

import com.zzang.chongdae.event.domain.ParticipateEvent;
import com.zzang.chongdae.notification.service.FcmNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class FcmEventListener {

    private final FcmNotificationService notificationService;

    @EventListener
    @Async
    public void handleParticipateEvent(ParticipateEvent event) {
        notificationService.participate(event.getOfferingMember());
    }
}
