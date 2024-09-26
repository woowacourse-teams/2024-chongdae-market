package com.zzang.chongdae.comment.service.dto;

import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;

public record CommentRoomAllResponseItem(Long offeringId,
                                         String offeringTitle,
                                         Boolean isProposer,
                                         CommentLatestResponse latestComment) {

    public CommentRoomAllResponseItem(OfferingMemberEntity offeringMember,
                                      CommentLatestResponse latestComment) {
        this(offeringMember.getOffering().getId(),
                offeringMember.getOffering().getTitle(),
                offeringMember.isProposer(),
                latestComment);
    }

    public CommentRoomAllResponseItem(Long offeringId,
                                      OfferingMemberEntity offeringMember,
                                      CommentLatestResponse latestComment) {
        this(offeringId,
                "삭제된 공동구매입니다.",
                offeringMember.isProposer(),
                latestComment);
    }
}
