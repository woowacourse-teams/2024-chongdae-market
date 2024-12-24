package com.zzang.chongdae.event.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.zzang.chongdae.event.domain.CancelParticipateEvent;
import com.zzang.chongdae.event.domain.DeleteOfferingEvent;
import com.zzang.chongdae.event.domain.LoginEvent;
import com.zzang.chongdae.event.domain.ParticipateEvent;
import com.zzang.chongdae.event.domain.SaveCommentEvent;
import com.zzang.chongdae.event.domain.SaveOfferingEvent;
import com.zzang.chongdae.event.domain.UpdateStatusEvent;
import com.zzang.chongdae.global.service.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class FcmEventListenerTest extends ServiceTest {

    @Autowired
    TestEventPublisher testEventPublisher;

    @MockBean
    FcmEventListener eventListener;

    @DisplayName("공모 참여 이벤트를 발행했을 때 이벤트 리스너는 해당 이벤트를 실행한다.")
    @Test
    void should_executeEvent_when_publishParticipateEvent() {
        // given
        ParticipateEvent event = mock(ParticipateEvent.class);

        // when
        testEventPublisher.publishWithTransaction(event);

        // then
        verify(eventListener, times(1)).handleParticipateEvent(event);
    }

    @DisplayName("공모 참여 취소 이벤트를 발행했을 때 이벤트 리스너는 해당 이벤트를 실행한다.")
    @Test
    void should_executeEvent_when_publishParticipateCancelEvent() {
        // given
        CancelParticipateEvent event = mock(CancelParticipateEvent.class);

        // when
        testEventPublisher.publishWithTransaction(event);

        // then
        verify(eventListener, times(1)).handleCancelParticipateEvent(event);
    }

    @DisplayName("공모 작성 이벤트를 발행했을 때 이벤트 리스너는 해당 이벤트를 실행한다.")
    @Test
    void should_executeEvent_when_publishSaveOfferingEvent() {
        // given
        SaveOfferingEvent event = mock(SaveOfferingEvent.class);

        // when
        testEventPublisher.publishWithoutTransaction(event);

        // then
        verify(eventListener, times(1)).handleSaveOfferingEvent(event);
    }

    @DisplayName("공모 삭제 이벤트를 발행했을 때 이벤트 리스너는 해당 이벤트를 실행한다.")
    @Test
    void should_executeEvent_when_publishDeleteOfferingEvent() {
        // given
        DeleteOfferingEvent event = mock(DeleteOfferingEvent.class);

        // when
        testEventPublisher.publishWithTransaction(event);

        // then
        verify(eventListener, times(1)).handleDeleteOfferingEvent(event);
    }

    @DisplayName("댓글 작성 이벤트를 발행했을 때 이벤트 리스너는 해당 이벤트를 실행한다.")
    @Test
    void should_executeEvent_when_publishSaveCommentEvent() {
        // given
        SaveCommentEvent event = mock(SaveCommentEvent.class);

        // when
        testEventPublisher.publishWithoutTransaction(event);

        // then
        verify(eventListener, times(1)).handleSaveCommentEvent(event);
    }

    @DisplayName("댓글방 상태 변경 이벤트를 발행했을 때 이벤트 리스너는 해당 이벤트를 실행한다.")
    @Test
    void should_executeEvent_when_publishUpdateStatusEvent() {
        // given
        UpdateStatusEvent event = mock(UpdateStatusEvent.class);

        // when
        testEventPublisher.publishWithTransaction(event);

        // then
        verify(eventListener, times(1)).handleUpdateStatusEvent(event);
    }

    @DisplayName("로그인 이벤트를 발행했을 때 이벤트 리스너는 해당 이벤트를 실행한다.")
    @Test
    void should_executeEvent_when_publishLoginEvent() {
        // given
        LoginEvent event = mock(LoginEvent.class);

        // when
        testEventPublisher.publishWithTransaction(event);

        // then
        verify(eventListener, times(1)).handleLoginEvent(event);
    }

    @DisplayName("이벤트 발행 후 예외가 발생한 경우 이벤트 로직을 실행하지 않는다.")
    @Test
    void should_notExecuteEvent_when_throwException() {
        // given
        LoginEvent event = mock(LoginEvent.class);

        // when
        try {
            testEventPublisher.publishWithTransactionThenThrowException(event);
        } catch (Exception ignored) {
        }

        // then
        verify(eventListener, times(0)).handleLoginEvent(any(LoginEvent.class));
    }
}
