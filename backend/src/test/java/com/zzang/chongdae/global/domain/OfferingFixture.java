package com.zzang.chongdae.global.domain;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OfferingFixture {

    @Autowired
    private OfferingRepository offeringRepository;

    public OfferingEntity createOffering(MemberEntity member) {
        OfferingEntity offering = new OfferingEntity(
                member,
                "title",
                "description",
                "thumbnailUrl",
                "productUrl",
                LocalDateTime.of(3000, 1, 1, 0, 0, 0),
                "meetingAddress",
                "meetingAddressDetail",
                "meetingAddressDong",
                5,
                1,
                false,
                5000,
                1000,
                CommentRoomStatus.GROUPING
        );
        return offeringRepository.save(offering);
    }
}
