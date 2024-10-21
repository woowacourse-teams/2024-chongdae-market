package com.zzang.chongdae.notification.domain;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FcmTopic {

    private static final String TOPIC_FORMAT_MEMBER = "member";
    private static final String TOPIC_FORMAT_OFFERING = "offering_%d";
    private static final String TOPIC_FORMAT_OFFERING_PROPOSER = "proposer_of_offering_%d";

    private final String value;

    public static FcmTopic offeringTopic(OfferingEntity offering) {
        String value = TOPIC_FORMAT_OFFERING.formatted(offering.getId());
        return new FcmTopic(value);
    }

    public static FcmTopic proposerTopic(OfferingEntity offering) {
        String value = TOPIC_FORMAT_OFFERING_PROPOSER.formatted(offering.getId());
        return new FcmTopic(value);
    }

    public static FcmTopic memberTopic() {
        return new FcmTopic(TOPIC_FORMAT_MEMBER);
    }
}
