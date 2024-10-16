package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.Message;

public interface NotificationSender {

    String send(Message message);
}
