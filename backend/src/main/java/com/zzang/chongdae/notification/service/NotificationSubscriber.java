package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.TopicManagementResponse;
import com.zzang.chongdae.member.repository.entity.MemberEntity;

public interface NotificationSubscriber { // todo: return data 제거

    TopicManagementResponse subscribe(MemberEntity member, String topic);

    TopicManagementResponse unsubscribe(MemberEntity member, String topic);
}
