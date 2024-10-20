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
        FcmData data = new FcmData();
        data.addData("title", comment.getOffering().getTitle());
        data.addData("body", "%s: %s".formatted(comment.getMember().getNickname(), comment.getContent()));
        return messageManager.createMessages(tokens, data);
    }

    private List<MemberEntity> membersNotWriter() {
        return members.stream()
                .filter(member -> !member.isSame(comment.getMember()))
                .toList();
    }
}
