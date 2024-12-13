package com.zzang.chongdae.global.domain;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OfferingFixture {

    @Autowired
    private OfferingRepository offeringRepository;

    private OfferingEntity createOffering(MemberEntity member,
                                          String title,
                                          Integer totalCount,
                                          Double discountRate,
                                          OfferingStatus offeringStatus,
                                          CommentRoomStatus commentRoomStatus) {
        OfferingEntity offering = new OfferingEntity(
                member,
                title,
                "description",
                "thumbnailUrl",
                "productUrl",
                LocalDateTime.of(3000, 1, 1, 0, 0, 0),
                "meetingAddress",
                "meetingAddressDetail",
                "meetingAddressDong",
                totalCount,
                1,
                5000,
                10_000,
                discountRate,
                offeringStatus,
                commentRoomStatus
        );
        return offeringRepository.save(offering);
    }

    public OfferingEntity createOffering(MemberEntity member, Double discountRate) {
        return createOffering(member, "title", 5, discountRate, OfferingStatus.AVAILABLE, CommentRoomStatus.GROUPING);
    }

    public OfferingEntity createOffering(MemberEntity member, CommentRoomStatus commentRoomStatus) {
        return createOffering(member, "title", 5, 33.3, OfferingStatus.AVAILABLE, commentRoomStatus);
    }

    public OfferingEntity createOffering(MemberEntity member, OfferingStatus offeringStatus) {
        return createOffering(member, "title", 5, 33.3, offeringStatus, CommentRoomStatus.GROUPING);
    }

    public OfferingEntity createOffering(MemberEntity member) {
        return createOffering(member, "title", 5, 33.3, OfferingStatus.AVAILABLE, CommentRoomStatus.GROUPING);
    }

    public OfferingEntity createOffering(MemberEntity member, String title) {
        return createOffering(member, title, 5, 33.3, OfferingStatus.AVAILABLE, CommentRoomStatus.GROUPING);
    }

    public OfferingEntity createOffering(MemberEntity member, Integer totalCount) {
        return createOffering(member, "title", totalCount, 33.3, OfferingStatus.AVAILABLE, CommentRoomStatus.GROUPING);
    }

    public void deleteOffering(OfferingEntity offering) {
        offeringRepository.delete(offering);
    }

    public void deleteOfferingById(Long offeringId) {
        offeringRepository.deleteById(offeringId);
    }

    public long countOffering() {
        return offeringRepository.count();
    }
}
