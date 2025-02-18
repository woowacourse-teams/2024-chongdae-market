package com.zzang.chongdae.notification.service.message;

import com.google.firebase.messaging.MulticastMessage;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.notification.domain.FcmData;
import com.zzang.chongdae.notification.domain.FcmTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentMessageManager {

    private static final String MESSAGE_BODY_FORMAT = "%s: %s";
    private static final String MESSAGE_TYPE = "comment_detail";

    private final FcmMessageCreator messageCreator;

    public MulticastMessage messageWhenSaveComment(CommentEntity comment, FcmTokens tokens) {
        FcmData data = new FcmData();
        data.addData("title", comment.getOffering().getTitle());
        data.addData("body", MESSAGE_BODY_FORMAT.formatted(comment.getMember().getNickname(), comment.getContent()));
        data.addData("offering_id", comment.getOffering().getId());
        data.addData("type", MESSAGE_TYPE);
        return messageCreator.createMessages(tokens, data);
    }
}
