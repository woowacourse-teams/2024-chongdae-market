package com.zzang.chongdae.notification.service.message;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.zzang.chongdae.notification.domain.FcmCondition;
import com.zzang.chongdae.notification.domain.FcmData;
import com.zzang.chongdae.notification.domain.FcmToken;
import com.zzang.chongdae.notification.domain.FcmTokens;
import com.zzang.chongdae.notification.domain.FcmTopic;
import org.springframework.stereotype.Component;

@Component
public class FcmMessageCreator {

    public Message createMessage(FcmToken token, FcmData data) {
        return Message.builder()
                .setToken(token.getValue())
                .putAllData(data.getData())
                .build();
    }

    public Message createMessage(FcmCondition condition, FcmData data) {
        return Message.builder()
                .setCondition(condition.getValue())
                .putAllData(data.getData())
                .build();
    }

    public Message createMessage(FcmTopic topic, FcmData data) {
        return Message.builder()
                .setTopic(topic.getValue())
                .putAllData(data.getData())
                .build();
    }

    public MulticastMessage createMessages(FcmTokens tokens, FcmData data) {
        return MulticastMessage.builder()
                .addAllTokens(tokens.getTokenValues())
                .putAllData(data.getData())
                .build();
    }
}
