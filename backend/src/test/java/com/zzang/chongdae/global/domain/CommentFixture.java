package com.zzang.chongdae.global.domain;

import com.zzang.chongdae.comment.repository.CommentRepository;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentFixture {

    @Autowired
    private CommentRepository commentRepository;

    public CommentEntity createComment(MemberEntity member, OfferingEntity offering) {
        CommentEntity comment = new CommentEntity(
                member,
                offering,
                "안녕하세요"
        );
        return commentRepository.save(comment);
    }
}
