package com.zzang.chongdae.offering.service;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.exception.MemberErrorCode;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.OfferingPrice;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponse;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponseItem;
import com.zzang.chongdae.offering.service.dto.OfferingCreateRequest;
import com.zzang.chongdae.offering.service.dto.OfferingDetailResponse;
import com.zzang.chongdae.offering.service.dto.OfferingMeetingResponse;
import com.zzang.chongdae.offeringmember.repository.OfferingMemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OfferingService {

    private final OfferingRepository offeringRepository;
    private final OfferingMemberRepository offeringMemberRepository;
    private final MemberRepository memberRepository;

    public OfferingDetailResponse getOfferingDetail(Long offeringId, Long memberId) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));

        OfferingPrice offeringPrice = offering.toOfferingPrice();
        OfferingStatus offeringStatus = offering.toOfferingStatus();

        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MarketException(MemberErrorCode.NOT_FOUND));
        Boolean isParticipated = offeringMemberRepository.existsByOfferingAndMember(offering, member);

        return new OfferingDetailResponse(offering, offeringPrice, offeringStatus, isParticipated);
    }

    public OfferingAllResponse getAllOffering(Long lastId, Integer pageSize) {
        Pageable pageable = PageRequest.ofSize(pageSize);
        List<OfferingEntity> offerings = offeringRepository.findByIdGreaterThan(lastId, pageable);
        return new OfferingAllResponse(offerings.stream()
                .map(offering -> {
                    OfferingPrice offeringPrice = offering.toOfferingPrice();
                    OfferingStatus offeringStatus = offering.toOfferingStatus();
                    return new OfferingAllResponseItem(offering, offeringPrice, offeringStatus);
                })
                .toList()
        );
    }

    public OfferingMeetingResponse getOfferingMeeting(Long offeringId) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        return new OfferingMeetingResponse(offering.toOfferingMeeting());
    }

    public Long createOffering(OfferingCreateRequest request) {
        MemberEntity member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new MarketException(MemberErrorCode.NOT_FOUND));
        OfferingEntity offering = new OfferingEntity(member, request);
        OfferingEntity savedOffering = offeringRepository.save(offering);
        return savedOffering.getId();
    }
}
