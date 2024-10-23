package com.zzang.chongdae.notification.service;

import com.google.firebase.messaging.TopicManagementResponse;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.domain.FcmTopic;

public interface NotificationSubscriber {

    TopicManagementResponse subscribe(MemberEntity member, FcmTopic topic);

    TopicManagementResponse unsubscribe(MemberEntity member, FcmTopic topic);
}
