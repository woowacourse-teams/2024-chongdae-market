package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.TopicManagementResponse;
import com.zzang.chongdae.member.repository.entity.MemberEntity;

public interface NotificationSubscriber {

    TopicManagementResponse subscribe(MemberEntity member, String topic);

    TopicManagementResponse unsubscribe(MemberEntity member, String topic);
}
