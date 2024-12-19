package com.zzang.chongdae.event.exception;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.event.domain.ParticipateEvent;
import com.zzang.chongdae.event.domain.SaveCommentEvent;
import com.zzang.chongdae.event.domain.SaveOfferingEvent;
import com.zzang.chongdae.event.service.FcmEventListener;
import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AsyncEventExceptionHandlerTest extends ServiceTest {

    @Autowired
    FcmEventListener eventListener;

    @DisplayName("비동기 로직 내 예외가 발생할 경우 반환하지 않고 로깅한다.")
    @Test
    void should_notReturnException_when_errorAsyncLogic() throws InterruptedException {
        // given
        MemberEntity proposer = memberFixture.createMember("proposer", "invalidProposerToken");
        MemberEntity participant1 = memberFixture.createMember("ever", "invalidEverToken");
        MemberEntity participant2 = memberFixture.createMember("whatever", "invalidWhateverToken");
        OfferingEntity offering = offeringFixture.createOffering(proposer);
        OfferingMemberEntity om2 = offeringMemberFixture.createParticipant(participant2, offering);
        OfferingMemberEntity om1 = offeringMemberFixture.createParticipant(participant1, offering);
        List<OfferingMemberEntity> offeringMembers = List.of(om1, om2);
        CommentEntity comment = commentFixture.createComment(proposer, offering);

        // when & then
        SaveOfferingEvent saveOfferingEvent = new SaveOfferingEvent(this, offering);
        SaveCommentEvent saveCommentEvent = new SaveCommentEvent(this, comment, offeringMembers);
        ParticipateEvent participateEvent = new ParticipateEvent(this, om2);

        assertAll(
                () -> assertDoesNotThrow(() -> eventListener.handleSaveOfferingEvent(saveOfferingEvent)),
                () -> assertDoesNotThrow(() -> eventListener.handleSaveCommentEvent(saveCommentEvent)),
                () -> assertDoesNotThrow(() -> eventListener.handleParticipateEvent(participateEvent))
        );
        Thread.sleep(3000);
    }
}
