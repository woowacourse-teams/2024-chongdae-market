package com.zzang.chongdae.offering.service;

import com.zzang.chongdae.offering.domain.OfferingPrice;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offering.service.dto.OfferingDetailResponse;
import com.zzang.chongdae.offeringmember.repository.OfferingMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OfferingService {

    private final OfferingRepository offeringRepository;
    private final OfferingMemberRepository offeringMemberRepository;

    public OfferingDetailResponse getOfferingDetail(Long id) {
        OfferingEntity offering = offeringRepository.findById(id)
                .orElseThrow(); // TODO: 예외 처리하기

        OfferingPrice offeringPrice = offering.toOfferingPrice();
        int currentCount = offeringMemberRepository.countByOffering(offering);
        OfferingStatus offeringStatus = offering.toOfferingStatus(currentCount);

        return new OfferingDetailResponse(offering, offeringPrice, offeringStatus);
    }
}
