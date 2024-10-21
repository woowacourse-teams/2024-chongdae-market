package com.zzang.chongdae.notification.service.message;

import com.google.firebase.messaging.MulticastMessage;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.domain.FcmData;
import com.zzang.chongdae.notification.domain.FcmTokens;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import java.util.List;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommentMessageManager {

    private static final String MESSAGE_BODY_FORMAT = "%s: %s";
    private static final String MESSAGE_TYPE = "comment_detail";

    private final FcmMessageCreator messageCreator;

    public CommentMessageManager() {
        this.messageCreator = FcmMessageCreator.getInstance();
    }

    @Nullable
    public MulticastMessage messageWhenSaveComment(CommentEntity comment, List<OfferingMemberEntity> offeringMembers) {
        FcmTokens tokens = FcmTokens.from(membersNotWriter(comment.getMember(), offeringMembers));
        if (tokens.isEmpty()) {
            return null;
        }
        FcmData data = new FcmData();
        data.addData("title", comment.getOffering().getTitle());
        data.addData("body", MESSAGE_BODY_FORMAT.formatted(comment.getMember().getNickname(), comment.getContent()));
        data.addData("offering_id", comment.getOffering().getId());
        data.addData("type", MESSAGE_TYPE);
        return messageCreator.createMessages(tokens, data);
    }

    private List<MemberEntity> membersNotWriter(MemberEntity writer, List<OfferingMemberEntity> offeringMembers) {
        return offeringMembers.stream()
                .map(OfferingMemberEntity::getMember)
                .filter(member -> !member.isSame(writer))
                .toList();
    }
}
