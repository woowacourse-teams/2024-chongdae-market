package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.zzang.chongdae.notification.domain.FcmCondition;
import com.zzang.chongdae.notification.domain.FcmToken;
import com.zzang.chongdae.notification.domain.FcmTokens;
import org.springframework.stereotype.Service;

@Service
public class FcmMessageManager {

    public Message createMessage(FcmToken token, String title, String body) {
        return Message.builder()
                .setToken(token.getValue())
                .setNotification(notification(title, body))
                .build();
    }

    public Message createMessage(FcmCondition condition, String title, String body) {
        return Message.builder()
                .setCondition(condition.getValue())
                .setNotification(
                        notification(title, body))
                .build();
    }

    public MulticastMessage createMessages(FcmTokens tokens, String title, String body) {
        return MulticastMessage.builder()
                .addAllTokens(tokens.getTokenValues())
                .setNotification(notification(title, body))
                .build();
    }

    private Notification notification(String title, String body) {
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }
}
