package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;

public interface NotificationSender {

    String send(Message message);

    BatchResponse send(MulticastMessage message);
}
