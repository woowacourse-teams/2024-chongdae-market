package com.zzang.chongdae.notification.domain;

import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import java.util.List;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentNotification {

    private final CommentEntity comment;
    private final List<MemberEntity> members;

    public CommentNotification(CommentEntity comment, List<OfferingMemberEntity> members) {
        this.comment = comment;
        this.members = members.stream()
                .map(OfferingMemberEntity::getMember)
                .toList();
    }

    @Nullable
    public MulticastMessage messageWhenSaveComment() {
        List<String> tokens = extractTokens();
        if (tokens.isEmpty()) {
            return null;
        }
        return MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(notification(comment.getOffering().getTitle(),
                        "%s: %s".formatted(comment.getMember().getNickname(), comment.getContent())))
                .build();
    }

    private List<String> extractTokens() {
        return members.stream()
                .filter(member -> !member.isSame(comment.getMember()))
                .map(MemberEntity::getFcmToken)
                .toList();
    }

    private Notification notification(String title, String body) {
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }
}
