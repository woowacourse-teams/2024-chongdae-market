package com.zzang.chongdae.offeringmember.service;

import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.OfferingProposer;
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

        int currentCount = offeringMemberRepository.countByOffering(offering);
        OfferingStatus offeringStatus = offering.toOfferingStatus(currentCount);
        OfferingProposer offeringProposer = offering.toOfferingProposer();
        validateParticipate(offeringStatus, offeringProposer, member);

        OfferingMemberEntity offeringMember = new OfferingMemberEntity(
                member, offering, OfferingMemberRole.PARTICIPANT);
        offeringMemberRepository.save(offeringMember);

        return offeringMember.getId();
    }

    private void validateParticipate(
            OfferingStatus offeringStatus, OfferingProposer offeringProposer, MemberEntity member) {
        validateClosed(offeringStatus);
        validateProposer(offeringProposer, member); // TODO: 이미 참여한 공모에 참여 못하게 추가로 막아야 함
    }

    private void validateClosed(OfferingStatus offeringStatus) {
        if (offeringStatus.isClosed()) {
            throw new IllegalArgumentException("아이고 못들어가요 ㅜㅜ"); // TODO: 예외처리 하기
        }
    }

    private void validateProposer(OfferingProposer offeringProposer, MemberEntity member) {
        if (offeringProposer.isProposer(member)) {
            throw new IllegalArgumentException("공모자는 참여할 수 없어요 ㅜㅜ"); // TODO: 예외처리 하기
        }
    }
}
