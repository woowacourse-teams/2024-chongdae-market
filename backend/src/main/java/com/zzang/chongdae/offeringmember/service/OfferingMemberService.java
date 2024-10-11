package com.zzang.chongdae.offeringmember.service;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.domain.OfferingMemberRole;
import com.zzang.chongdae.offeringmember.exception.OfferingMemberErrorCode;
import com.zzang.chongdae.offeringmember.repository.OfferingMemberRepository;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import com.zzang.chongdae.offeringmember.service.dto.ParticipationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OfferingMemberService {

    private final OfferingMemberRepository offeringMemberRepository;
    private final OfferingRepository offeringRepository;

    @Transactional
    public Long participate(ParticipationRequest request, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(request.offeringId())
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        validateParticipate(offering, member);

        OfferingMemberEntity offeringMember = new OfferingMemberEntity(
                member, offering, OfferingMemberRole.PARTICIPANT);
        offeringMemberRepository.save(offeringMember);
        offering.updateCurrentCount();
        return offeringMember.getId();
    }

    private void validateParticipate(OfferingEntity offering, MemberEntity member) {
        validateClosed(offering);
        validateDuplicate(offering, member);
    }

    private void validateClosed(OfferingEntity offering) {
        OfferingStatus offeringStatus = offering.toOfferingStatus();
        if (offeringStatus.isClosed()) {
            throw new MarketException(OfferingErrorCode.CANNOT_PARTICIPATE);
        }
    }

    private void validateDuplicate(OfferingEntity offering, MemberEntity member) {
        if (offeringMemberRepository.existsByOfferingAndMember(offering, member)) {
            throw new MarketException(OfferingMemberErrorCode.DUPLICATED);
        }
    }
}
