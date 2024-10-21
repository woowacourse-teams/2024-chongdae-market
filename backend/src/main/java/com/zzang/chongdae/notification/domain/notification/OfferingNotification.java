package com.zzang.chongdae.notification.domain.notification;

import com.google.firebase.messaging.Message;
import com.zzang.chongdae.notification.domain.FcmCondition;
import com.zzang.chongdae.notification.domain.FcmData;
import com.zzang.chongdae.notification.service.FcmMessageManager;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class OfferingNotification {

    private static final String MESSAGE_TITLE = "두근두근 새로운 공모를 확인해보세요!";
    private static final String MESSAGE_TYPE = "offering_detail";

    private final FcmMessageManager messageManager;
    private final OfferingEntity offering;

    public Message messageWhenSaveOffering() {
        FcmCondition condition = FcmCondition.offeringCondition(offering);
        FcmData data = new FcmData();
        data.addData("title", MESSAGE_TITLE);
        data.addData("body", offering.getTitle());
        data.addData("offering_id", offering.getId());
        data.addData("type", MESSAGE_TYPE);
        return messageManager.createMessage(condition, data);
    }
}
