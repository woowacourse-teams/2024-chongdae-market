package com.zzang.chongdae.event.service;

import com.zzang.chongdae.event.domain.CancelParticipateEvent;
import com.zzang.chongdae.event.domain.DeleteOfferingEvent;
import com.zzang.chongdae.event.domain.LoginEvent;
import com.zzang.chongdae.event.domain.ParticipateEvent;
import com.zzang.chongdae.event.domain.SaveCommentEvent;
import com.zzang.chongdae.event.domain.SaveOfferingEvent;
import com.zzang.chongdae.event.domain.UpdateStatusEvent;
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

    @EventListener
    @Async
    public void handleCancelParticipateEvent(CancelParticipateEvent event) {
        notificationService.cancelParticipation(event.getOfferingMember());
    }

    @EventListener
    @Async
    public void handleSaveOfferingEvent(SaveOfferingEvent event) {
        notificationService.saveOffering(event.getOffering());
    }

    @EventListener
    @Async
    public void handleDeleteOfferingEvent(DeleteOfferingEvent event) {
        notificationService.deleteOffering(event.getOffering());
    }

    @EventListener
    @Async
    public void handleSaveCommentEvent(SaveCommentEvent event) {
        notificationService.saveComment(event.getComment(), event.getOfferingMembers());
    }

    @EventListener
    @Async
    public void handleUpdateStatusEvent(UpdateStatusEvent event) {
        notificationService.updateStatus(event.getOffering());
    }

    @EventListener
    @Async
    public void handleLoginEvent(LoginEvent event) {
        notificationService.login(event.getMember());
    }
}
