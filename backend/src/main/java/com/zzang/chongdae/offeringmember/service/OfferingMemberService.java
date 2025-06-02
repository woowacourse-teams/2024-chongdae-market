package com.zzang.chongdae.offeringmember.service;

import com.zzang.chongdae.event.domain.CancelParticipateEvent;
import com.zzang.chongdae.event.domain.ParticipateEvent;
import com.zzang.chongdae.global.config.WriterDatabase;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.domain.OfferingMemberRole;
import com.zzang.chongdae.offeringmember.domain.OfferingMembers;
import com.zzang.chongdae.offeringmember.exception.OfferingMemberErrorCode;
import com.zzang.chongdae.offeringmember.repository.OfferingMemberRepository;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import com.zzang.chongdae.offeringmember.service.dto.ParticipantResponse;
import com.zzang.chongdae.offeringmember.service.dto.ParticipationRequest;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OfferingMemberService {

    private final OfferingMemberRepository offeringMemberRepository;
    private final OfferingRepository offeringRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final EntityManager entityManager;

    @WriterDatabase
    @Transactional
    public Long participate(ParticipationRequest request, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(request.offeringId())
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        validateParticipate(offering, member, request.participationCount());

        offering.participate(request.participationCount());
        entityManager.flush();

        OfferingMemberEntity offeringMember = new OfferingMemberEntity(
                member, offering, OfferingMemberRole.PARTICIPANT, request.participationCount());
        OfferingMemberEntity saved = offeringMemberRepository.save(offeringMember);

        eventPublisher.publishEvent(new ParticipateEvent(this, saved));
        return saved.getId();
    }

    private void validateParticipate(OfferingEntity offering, MemberEntity member, int participationCount) {
        validateClosed(offering);
        validateDuplicate(offering, member);
        validateParticipationCount(offering, participationCount);
    }

    private void validateClosed(OfferingEntity offering) {
        if (offering.getOfferingStatus().isClosed()) {
            throw new MarketException(OfferingErrorCode.CANNOT_PARTICIPATE);
        }
    }

    private void validateDuplicate(OfferingEntity offering, MemberEntity member) {
        if (offeringMemberRepository.existsByOfferingAndMember(offering, member)) {
            throw new MarketException(OfferingMemberErrorCode.DUPLICATED);
        }
    }

    private void validateParticipationCount(OfferingEntity offering, int participationCount) {
        if (offering.getCurrentCount() + participationCount > offering.getTotalCount()) {
            throw new MarketException(OfferingMemberErrorCode.INVALID_PARTICIPATION_COUNT);
        }
    }

    @WriterDatabase
    @Transactional
    public void cancelParticipate(Long offeringId, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findByIdWithDeleted(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        OfferingMemberEntity offeringMember = offeringMemberRepository.findByOfferingIdAndMember(offeringId, member)
                .orElseThrow(() -> new MarketException(OfferingMemberErrorCode.PARTICIPANT_NOT_FOUND));
        validateCancel(offeringMember);
        offeringMemberRepository.delete(offeringMember);
        offering.leave(offeringMember.getParticipationCount());

        eventPublisher.publishEvent(new CancelParticipateEvent(this, offeringMember));
    }

    private void validateCancel(OfferingMemberEntity offeringMember) {
        validateIsProposer(offeringMember);
        validateInProgress(offeringMember);
    }

    private void validateIsProposer(OfferingMemberEntity offeringMember) {
        OfferingEntity offering = offeringMember.getOffering();
        CommentRoomStatus roomStatus = offering.getRoomStatus();
        if (offeringMember.isProposer() && (roomStatus.isInProgress() || roomStatus.isGrouping())) {
            throw new MarketException(OfferingMemberErrorCode.CANNOT_CANCEL_PROPOSER);
        }
    }

    private void validateInProgress(OfferingMemberEntity offeringMember) {
        OfferingEntity offering = offeringMember.getOffering();
        CommentRoomStatus roomStatus = offering.getRoomStatus();
        if (roomStatus.isInProgress()) {
            throw new MarketException(OfferingMemberErrorCode.CANNOT_CANCEL_IN_PROGRESS);
        }
    }

    public ParticipantResponse getAllParticipant(Long offeringId, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findByIdWithDeleted(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        validateParticipants(offering, member);

        List<OfferingMemberEntity> offeringMembers = offeringMemberRepository.findAllWithMemberByOffering(offering);
        OfferingMembers members = new OfferingMembers(offeringMembers);
        return ParticipantResponse.from(offering, members);
    }

    private void validateParticipants(OfferingEntity offering, MemberEntity member) {
        if (!offeringMemberRepository.existsByOfferingAndMember(offering, member)) {
            throw new MarketException(OfferingMemberErrorCode.PARTICIPANT_NOT_FOUND);
        }
    }
}
