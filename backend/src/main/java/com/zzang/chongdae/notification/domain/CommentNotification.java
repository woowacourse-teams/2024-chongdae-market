package com.zzang.chongdae.notification.domain;

import com.google.firebase.messaging.MulticastMessage;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.service.FcmMessageManager;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import java.util.List;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentNotification {

    private final FcmMessageManager messageManager;
    private final CommentEntity comment;
    private final List<MemberEntity> members;

    public CommentNotification(FcmMessageManager messageManager, CommentEntity comment,
                               List<OfferingMemberEntity> members) {
        this.messageManager = messageManager;
        this.comment = comment;
        this.members = members.stream()
                .map(OfferingMemberEntity::getMember)
                .toList();
    }

    @Nullable
    public MulticastMessage messageWhenSaveComment() {
        FcmTokens tokens = FcmTokens.from(membersNotWriter());
        if (tokens.isEmpty()) {
            return null;
        }
        return messageManager.createMessages(
                tokens,
                comment.getOffering().getTitle(),
                "%s: %s".formatted(comment.getMember().getNickname(), comment.getContent()));
    }

    private List<MemberEntity> membersNotWriter() {
        return members.stream()
                .filter(member -> !member.isSame(comment.getMember()))
                .toList();
    }
}
