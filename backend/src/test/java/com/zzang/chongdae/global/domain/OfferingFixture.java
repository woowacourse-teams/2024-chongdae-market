package com.zzang.chongdae.global.domain;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.math.BigDecimal;
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
                LocalDateTime.of(2000, 4, 7, 0, 0, 0),
                "meetingAddress",
                "meetingAddressDetail",
                5,
                1,
                false,
                BigDecimal.valueOf(5000)
        );
        return offeringRepository.save(offering);
    }
}
