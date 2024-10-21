package com.zzang.chongdae.notification.domain;

import com.google.firebase.messaging.Message;
import com.zzang.chongdae.notification.service.FcmMessageManager;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RoomStatusNotification {

    public static final String TOPIC_FORMAT_OFFERING = "offering_%d"; // todo: topic 도메인 추출
    public static final String TOPIC_FORMAT_OFFERING_PROPOSER = "proposer_%d";

    private static final String CONDITION_FORMAT = "'%s' in topics && !('%s' in topics)"; // todo: 부정문 가능한지 안드 테스트

    private final FcmMessageManager messageManager;
    private final OfferingEntity offering;

    public Message messageWhenUpdateStatus() {
        FcmCondition condition = createCondition();
        FcmData data = new FcmData();
        data.addData("title", offering.getTitle());
        data.addData("body", "거래 상태가 %s로 변경되었습니다.".formatted(offering.getRoomStatus()));
        data.addData("offering_id", offering.getId());
        return messageManager.createMessage(condition, data);
    }

    private FcmCondition createCondition() {
        String offeringTopic = TOPIC_FORMAT_OFFERING.formatted(offering.getId());
        String isProposerTopic = TOPIC_FORMAT_OFFERING_PROPOSER.formatted(offering.getId());
        String condition = CONDITION_FORMAT.formatted(offeringTopic, isProposerTopic);
        log.info("[{}] 조건에 보내는 메시지", condition);
        return new FcmCondition(condition);
    }
}
