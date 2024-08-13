package com.zzang.chongdae.offeringmember.service;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.domain.OfferingMemberRole;
import com.zzang.chongdae.offeringmember.domain.OfferingMembers;
import com.zzang.chongdae.offeringmember.exception.OfferingMemberErrorCode;
import com.zzang.chongdae.offeringmember.repository.OfferingMemberRepository;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import com.zzang.chongdae.offeringmember.service.dto.ParticipantCountResponseItem;
import com.zzang.chongdae.offeringmember.service.dto.ParticipantPriceResponseItem;
import com.zzang.chongdae.offeringmember.service.dto.ParticipantResponse;
import com.zzang.chongdae.offeringmember.service.dto.ParticipantResponseItem;
import com.zzang.chongdae.offeringmember.service.dto.ParticipationRequest;
import com.zzang.chongdae.offeringmember.service.dto.ProposerResponseItem;
import java.util.List;
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

    public ParticipantResponse getAllParticipant(Long offeringId, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        validateParticipants(offering, member);

        List<OfferingMemberEntity> offeringMembers = offeringMemberRepository.findAllByOffering(offering);
        OfferingMembers members = new OfferingMembers(offeringMembers);
        MemberEntity proposer = members.getProposer();
        List<MemberEntity> participants = members.getParticipants();

        ProposerResponseItem proposerResponseItem = new ProposerResponseItem(proposer);
        List<ParticipantResponseItem> participantsResponseItem = participants.stream()
                .map(ParticipantResponseItem::new)
                .toList();
        ParticipantCountResponseItem countResponseItem
                = new ParticipantCountResponseItem(offering.getCurrentCount(), offering.getTotalCount());
        ParticipantPriceResponseItem estimatedPriceItem
                = new ParticipantPriceResponseItem(offering.toOfferingPrice().calculateDividedPrice());
        return new ParticipantResponse(
                proposerResponseItem, participantsResponseItem,countResponseItem, estimatedPriceItem);
    }

    private void validateParticipants(OfferingEntity offering, MemberEntity member) {
        if (!offeringMemberRepository.existsByOfferingAndMember(offering, member)) {
            throw new MarketException(OfferingMemberErrorCode.PARTICIPANT_NOT_FOUND);
        }
    }
}
