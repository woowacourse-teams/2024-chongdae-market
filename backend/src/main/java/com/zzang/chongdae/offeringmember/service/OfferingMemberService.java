package com.zzang.chongdae.offeringmember.service;

import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.domain.OfferingMemberRole;
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
    private final MemberRepository memberRepository;
    private final OfferingRepository offeringRepository;

    @Transactional
    public Long participate(ParticipationRequest request) {
        MemberEntity member = memberRepository.findById(request.memberId())
                .orElseThrow(); // TODO: 예외처리 하기
        OfferingEntity offering = offeringRepository.findById(request.offeringId())
                .orElseThrow();// TODO: 예외처리 하기
        validateParticipate(offering, member);

        OfferingMemberEntity offeringMember = new OfferingMemberEntity(
                member, offering, OfferingMemberRole.PARTICIPANT);
        offeringMemberRepository.save(offeringMember);
        return offeringMember.getId();
    }

    private void validateParticipate(OfferingEntity offering, MemberEntity member) {
        validateClosed(offering);
        validateDuplicate(offering, member);
    }

    private void validateClosed(OfferingEntity offering) {
        int currentCount = offeringMemberRepository.countByOffering(offering);
        OfferingStatus offeringStatus = offering.toOfferingStatus(currentCount);
        if (offeringStatus.isClosed()) {
            throw new IllegalArgumentException("아이고 못들어가요 ㅜㅜ"); // TODO: 예외처리 하기
        }
    }

    private void validateDuplicate(OfferingEntity offering, MemberEntity member) {
        if (offeringMemberRepository.existsByOfferingAndMember(offering, member)) {
            throw new IllegalArgumentException("이미 참여한 공모엔 참여할 수 없습니다."); // TODO: 예외처리 하기
        }
    }
}
