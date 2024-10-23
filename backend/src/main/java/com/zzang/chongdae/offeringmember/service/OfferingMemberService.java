package com.zzang.chongdae.offeringmember.service;

import com.zzang.chongdae.event.domain.ParticipateEvent;
import com.zzang.chongdae.global.config.WriterDatabase;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.service.FcmNotificationService;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
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
import com.zzang.chongdae.offeringmember.service.dto.ParticipantResponse;
import com.zzang.chongdae.offeringmember.service.dto.ParticipantResponseItem;
import com.zzang.chongdae.offeringmember.service.dto.ParticipationRequest;
import com.zzang.chongdae.offeringmember.service.dto.ProposerResponseItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OfferingMemberService {

    private final FcmNotificationService notificationService;
    private final OfferingMemberRepository offeringMemberRepository;
    private final OfferingRepository offeringRepository;
    private final ApplicationEventPublisher eventPublisher;

    @WriterDatabase
    @Transactional
    public Long participate(ParticipationRequest request, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(request.offeringId())
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        validateParticipate(offering, member);

        OfferingMemberEntity offeringMember = new OfferingMemberEntity(
                member, offering, OfferingMemberRole.PARTICIPANT);
        OfferingMemberEntity saved = offeringMemberRepository.save(offeringMember);

        offering.participate();
        OfferingStatus offeringStatus = offering.toOfferingJoinedCount().decideOfferingStatus();
        offering.updateOfferingStatus(offeringStatus);

        eventPublisher.publishEvent(new ParticipateEvent(this, saved));
        return offeringMember.getId();
    }

    private void validateParticipate(OfferingEntity offering, MemberEntity member) {
        validateClosed(offering);
        validateDuplicate(offering, member);
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

    @WriterDatabase
    @Transactional
    public void cancelParticipate(Long offeringId, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        OfferingMemberEntity offeringMember = offeringMemberRepository.findByOfferingAndMember(offering, member)
                .orElseThrow(() -> new MarketException(OfferingMemberErrorCode.PARTICIPANT_NOT_FOUND));
        validateCancel(offeringMember);
        offeringMemberRepository.delete(offeringMember);

        offering.leave();
        OfferingStatus offeringStatus = offering.toOfferingJoinedCount().decideOfferingStatus();
        offering.updateOfferingStatus(offeringStatus);
        notificationService.cancelParticipation(offeringMember);
    }

    private void validateCancel(OfferingMemberEntity offeringMember) {
        validateIsProposer(offeringMember);
        validateInProgress(offeringMember);
    }

    private void validateIsProposer(OfferingMemberEntity offeringMember) {
        if (offeringMember.isProposer()) {
            throw new MarketException(OfferingMemberErrorCode.CANNOT_CANCEL_PROPOSER);
        }
    }

    private void validateInProgress(OfferingMemberEntity offeringMember) {
        OfferingEntity offering = offeringMember.getOffering();
        CommentRoomStatus roomStatus = offering.getRoomStatus();
        if (roomStatus.isGrouped()) {
            throw new MarketException(OfferingMemberErrorCode.CANNOT_CANCEL_IN_PROGRESS);
        }
    }

    public ParticipantResponse getAllParticipant(Long offeringId, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findByIdWithDeleted(offeringId)
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
        ParticipantCountResponseItem countResponseItem = new ParticipantCountResponseItem(offering);
        Integer estimatedPrice = offering.toOfferingPrice().calculateDividedPrice();
        return new ParticipantResponse(
                proposerResponseItem, participantsResponseItem, countResponseItem, estimatedPrice);
    }

    private void validateParticipants(OfferingEntity offering, MemberEntity member) {
        if (!offeringMemberRepository.existsByOfferingAndMember(offering, member)) {
            throw new MarketException(OfferingMemberErrorCode.PARTICIPANT_NOT_FOUND);
        }
    }
}
