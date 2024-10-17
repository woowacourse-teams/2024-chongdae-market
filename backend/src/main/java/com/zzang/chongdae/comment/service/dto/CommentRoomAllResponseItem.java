package com.zzang.chongdae.comment.service.dto;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;

public record CommentRoomAllResponseItem(Long offeringId,
                                         String offeringTitle,
                                         Boolean isProposer,
                                         CommentLatestResponse latestComment) implements Comparable<CommentRoomAllResponseItem>{

    public CommentRoomAllResponseItem(OfferingEntity offering,
                                      OfferingMemberEntity offeringMember,
                                      CommentLatestResponse latestComment) {
        this(offering.getId(),
                offering.getTitle(),
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

    @Override
    public int compareTo(CommentRoomAllResponseItem other) {
        return this.latestComment.createdAt().compareTo(other.latestComment().createdAt());
    }
}
