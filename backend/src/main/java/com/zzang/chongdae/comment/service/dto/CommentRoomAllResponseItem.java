package com.zzang.chongdae.comment.service.dto;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;

public record CommentRoomAllResponseItem(Long offeringId,
                                         String offeringTitle,
                                         Boolean isProposer,
                                         CommentLatestResponse latestComment) {

    public CommentRoomAllResponseItem(OfferingEntity offering,
                                      MemberEntity member,
                                      CommentLatestResponse latestComment) {
        this(offering.getId(),
                offering.getTitle(),
                offering.isProposedBy(member),
                latestComment);
    }
}
