package com.zzang.chongdae.event.service;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

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
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class FcmEventListener {

    private final FcmNotificationService notificationService;

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Async
    public void handleParticipateEvent(ParticipateEvent event) {
        notificationService.participate(event.getOfferingMember());
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Async
    public void handleCancelParticipateEvent(CancelParticipateEvent event) {
        notificationService.cancelParticipation(event.getOfferingMember());
    }

    @EventListener
    @Async
    public void handleSaveOfferingEvent(SaveOfferingEvent event) {
        notificationService.saveOffering(event.getOffering());
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Async
    public void handleDeleteOfferingEvent(DeleteOfferingEvent event) {
        notificationService.deleteOffering(event.getOffering());
    }

    @EventListener
    @Async
    public void handleSaveCommentEvent(SaveCommentEvent event) {
        notificationService.saveComment(event.getComment(), event.getOfferingMembers());
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Async
    public void handleUpdateStatusEvent(UpdateStatusEvent event) {
        notificationService.updateStatus(event.getOffering());
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Async
    public void handleLoginEvent(LoginEvent event) {
        notificationService.login(event.getMember());
    }
}
