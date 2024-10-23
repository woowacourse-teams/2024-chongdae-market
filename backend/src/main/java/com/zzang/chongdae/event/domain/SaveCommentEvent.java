package com.zzang.chongdae.event.domain;

import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import java.util.List;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SaveCommentEvent extends ApplicationEvent {

    private final CommentEntity comment;
    private final List<OfferingMemberEntity> offeringMembers;

    public SaveCommentEvent(Object source, CommentEntity comment, List<OfferingMemberEntity> offeringMembers) {
        super(source);
        this.comment = comment;
        this.offeringMembers = offeringMembers;
    }
}
