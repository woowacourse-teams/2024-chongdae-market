package com.zzang.chongdae.notification.domain;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FcmCondition {

    private static final String CONDITION_FORMAT_TRUE_AND_FALSE = "'%s' in topics && !('%s' in topics)";

    private final String value;

    public static FcmCondition offeringCondition(OfferingEntity offering) {
        FcmTopic memberTopic = FcmTopic.memberTopic();
        FcmTopic proposerTopic = FcmTopic.proposerTopic(offering);
        String value = CONDITION_FORMAT_TRUE_AND_FALSE.formatted(memberTopic.getValue(), proposerTopic.getValue());
        return new FcmCondition(value);
    }
}
