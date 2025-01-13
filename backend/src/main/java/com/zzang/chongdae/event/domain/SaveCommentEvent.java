package com.zzang.chongdae.event.domain;

import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.domain.FcmTokens;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import java.util.List;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SaveCommentEvent extends ApplicationEvent {

    private final CommentEntity comment;
    private final FcmTokens tokens;

    public SaveCommentEvent(Object source, CommentEntity comment, List<OfferingMemberEntity> offeringMembers) {
        super(source);
        this.comment = comment;
        this.tokens = createFcmTokens(comment.getMember(), offeringMembers);
    }

    private FcmTokens createFcmTokens(MemberEntity writer, List<OfferingMemberEntity> offeringMembers) {
        List<MemberEntity> membersNotWriter = offeringMembers.stream()
                .map(OfferingMemberEntity::getMember)
                .filter(member -> !member.isSame(writer))
                .toList();
        return FcmTokens.from(membersNotWriter);
    }
}
