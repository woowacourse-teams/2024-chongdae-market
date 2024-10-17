package com.zzang.chongdae.notification.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.firebase.messaging.BatchResponse;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FcmNotificationTest extends ServiceTest {

    private final String proposerToken = "youHaveToChangeThis1";
    private final String participant1Token = "youHaveToChangeThis2";
    private final String participant2Token = "youHaveToChangeThis3";

    @Autowired
    private FcmNotificationService notificationService;

    @Disabled
    @DisplayName("FCM에 메시지를 전송할 수 있다.")
    @Test
    void should_sendNotificationToFcm() {
        // given
        MemberEntity proposer = memberFixture.createMember("proposer", proposerToken);
        MemberEntity participant = memberFixture.createMember("ever", participant1Token);

        OfferingEntity offering = offeringFixture.createOffering(proposer);
        offeringMemberFixture.createProposer(proposer, offering);
        OfferingMemberEntity offeringMember = offeringMemberFixture.createParticipant(participant, offering);

        // when
        String messageId = notificationService.participate(offeringMember);

        // then
        assertThat(messageId).contains("messages");
    }

    @Disabled
    @DisplayName("FCM에 대량 메시지를 전송할 수 있다.")
    @Test
    void should_sendNotificationsToFcm() {
        // given
        MemberEntity proposer = memberFixture.createMember("proposer", proposerToken);
        MemberEntity participant1 = memberFixture.createMember("ever1", participant1Token);
        MemberEntity participant2 = memberFixture.createMember("ever2", participant2Token);

        OfferingEntity offering = offeringFixture.createOffering(proposer);
        OfferingMemberEntity proposerOfferingMember = offeringMemberFixture.createProposer(proposer, offering);
        OfferingMemberEntity participant1OfferingMember = offeringMemberFixture.createParticipant(participant1,
                offering);
        OfferingMemberEntity participant2OfferingMember = offeringMemberFixture.createParticipant(participant2,
                offering);
        CommentEntity comment = commentFixture.createComment(proposer, offering);

        // when
        BatchResponse batchResponse = notificationService.saveComment(comment,
                List.of(proposerOfferingMember, participant1OfferingMember, participant2OfferingMember));

        // then
        assertThat(batchResponse).isNotNull();
        assertThat(batchResponse.getSuccessCount()).isEqualTo(2);
    }

    @Disabled
    @DisplayName("참여자가 없는 방에 댓글을 작성할 경우 메시지를 전송하지 않는다.")
    @Test
    void should_notSendNotificationsToFcm_when_noParticipants() {
        // given
        MemberEntity proposer = memberFixture.createMember("proposer", proposerToken);

        OfferingEntity offering = offeringFixture.createOffering(proposer);
        OfferingMemberEntity proposerOfferingMember = offeringMemberFixture.createProposer(proposer, offering);
        CommentEntity comment = commentFixture.createComment(proposer, offering);

        // when
        BatchResponse batchResponse = notificationService.saveComment(comment, List.of(proposerOfferingMember));

        // then
        assertThat(batchResponse).isNull();
    }
}
