package com.zzang.chongdae.offering.service;

import com.zzang.chongdae.event.domain.DeleteOfferingEvent;
import com.zzang.chongdae.event.domain.SaveOfferingEvent;
import com.zzang.chongdae.global.config.WriterDatabase;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.OfferingFilter;
import com.zzang.chongdae.offering.domain.OfferingJoinedCount;
import com.zzang.chongdae.offering.domain.OfferingMeeting;
import com.zzang.chongdae.offering.domain.OfferingPrice;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.domain.UpdatedOffering;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponse;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponseItem;
import com.zzang.chongdae.offering.service.dto.OfferingDetailResponse;
import com.zzang.chongdae.offering.service.dto.OfferingFilterAllResponse;
import com.zzang.chongdae.offering.service.dto.OfferingFilterAllResponseItem;
import com.zzang.chongdae.offering.service.dto.OfferingMeetingResponse;
import com.zzang.chongdae.offering.service.dto.OfferingMeetingUpdateRequest;
import com.zzang.chongdae.offering.service.dto.OfferingMetaResponse;
import com.zzang.chongdae.offering.service.dto.OfferingProductImageRequest;
import com.zzang.chongdae.offering.service.dto.OfferingProductImageResponse;
import com.zzang.chongdae.offering.service.dto.OfferingSaveRequest;
import com.zzang.chongdae.offering.service.dto.OfferingUpdateRequest;
import com.zzang.chongdae.offering.service.dto.OfferingUpdateResponse;
import com.zzang.chongdae.offeringmember.domain.OfferingMemberRole;
import com.zzang.chongdae.offeringmember.repository.OfferingMemberRepository;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import com.zzang.chongdae.storage.service.StorageService;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class OfferingService {

    private final ApplicationEventPublisher eventPublisher;
    private final OfferingRepository offeringRepository;
    private final OfferingMemberRepository offeringMemberRepository;
    private final StorageService storageService;
    private final ProductImageExtractor imageExtractor;
    private final OfferingFetcher offeringFetcher;
    private final Clock clock;

    @Transactional(readOnly = true)
    public OfferingDetailResponse getOfferingDetail(Long offeringId, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        OfferingPrice offeringPrice = offering.toOfferingPrice();
        OfferingJoinedCount offeringJoinedCount = offering.toOfferingJoinedCount();
        Boolean isProposer = offering.isProposedBy(member); // TODO: 추후 도메인으로 분리
        Boolean isParticipated = offeringMemberRepository.existsByOfferingAndMember(offering, member);
        return new OfferingDetailResponse(
                offering, offeringPrice, offeringJoinedCount, isProposer, isParticipated);
    }

    public OfferingAllResponseItem getOffering(Long offeringId) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        OfferingPrice offeringPrice = offering.toOfferingPrice();
        return new OfferingAllResponseItem(offering, offeringPrice);
    }

    @Transactional(readOnly = true)
    public OfferingAllResponse getAllOffering(String filterName, String searchKeyword, Long lastId, Integer pageSize) {
        Pageable pageable = PageRequest.ofSize(pageSize);
        OfferingFilter filter = OfferingFilter.findByName(filterName);
        List<OfferingEntity> offerings = offeringFetcher.fetchOfferings(filter, searchKeyword, lastId, pageable);
        return new OfferingAllResponse(offerings.stream()
                .map(offering -> {
                    OfferingPrice offeringPrice = offering.toOfferingPrice();
                    return new OfferingAllResponseItem(offering, offeringPrice);
                })
                .toList()
        );
    }

    public OfferingFilterAllResponse getAllOfferingFilter() {
        List<OfferingFilterAllResponseItem> filters = Arrays.stream(OfferingFilter.values())
                .map(OfferingFilterAllResponseItem::new)
                .toList();
        return new OfferingFilterAllResponse(filters);
    }

    public OfferingMeetingResponse getOfferingMeeting(Long offeringId, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findByIdWithDeleted(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        validateIsParticipant(member, offering);
        return new OfferingMeetingResponse(offering.toOfferingMeeting());
    }

    private void validateIsParticipant(MemberEntity member, OfferingEntity offering) {
        if (!offeringMemberRepository.existsByOfferingAndMember(offering, member)) {
            throw new MarketException(OfferingErrorCode.NOT_PARTICIPATE_MEMBER);
        }
    }

    @WriterDatabase
    @Transactional
    public OfferingMeetingResponse updateOfferingMeeting(
            Long offeringId, OfferingMeetingUpdateRequest request, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        validateIsProposer(offering, member);
        validateMeetingDate(request.meetingDate());
        OfferingMeeting offeringMeeting = request.toOfferingMeeting();
        offering.updateMeeting(offeringMeeting);
        return new OfferingMeetingResponse(offering.toOfferingMeeting());
    }

    private void validateIsProposer(OfferingEntity offering, MemberEntity member) {
        if (offering.isNotProposedBy(member)) {
            throw new MarketException(OfferingErrorCode.NOT_PROPOSE_MEMBER);
        }
    }

    @WriterDatabase
    public Long saveOffering(OfferingSaveRequest request, MemberEntity member) {
        OfferingEntity offering = request.toEntity(member);
        validateOffering(request, offering);
        OfferingEntity saved = offeringRepository.save(offering);

        OfferingMemberEntity offeringMember = new OfferingMemberEntity(member, saved, OfferingMemberRole.PROPOSER,
                request.myCount());
        offeringMemberRepository.save(offeringMember);

        eventPublisher.publishEvent(new SaveOfferingEvent(this, saved));
        return saved.getId();
    }

    private void validateOffering(OfferingSaveRequest request, OfferingEntity offering) {
        validateMeetingDate(offering.getMeetingDate());
        validateCount(request.myCount(), request.totalCount());
    }

    private void validateMeetingDate(LocalDateTime offeringMeetingDateTime) {
        LocalDate thresholdDate = LocalDate.now(clock);
        LocalDate targetDate = offeringMeetingDateTime.toLocalDate();
        if (targetDate.isBefore(thresholdDate)) {
            throw new MarketException(OfferingErrorCode.CANNOT_MEETING_DATE_BEFORE_THAN_TODAY);
        }
    }

    private void validateCount(Integer myCount, Integer totalCount) {
        if (myCount != null && myCount >= totalCount) {
            throw new MarketException(OfferingErrorCode.INVALID_MY_COUNT);
        }
    }

    public OfferingProductImageResponse uploadProductImage(MultipartFile image) {
        String imageUrl = storageService.uploadFile(image);
        return new OfferingProductImageResponse(imageUrl);
    }

    public OfferingProductImageResponse extractProductImageFromOg(OfferingProductImageRequest request) {
        String imageUrl = imageExtractor.extract(request.productUrl());
        return new OfferingProductImageResponse(imageUrl);
    }

    @WriterDatabase
    @Transactional
    public OfferingUpdateResponse updateOffering(Long offeringId, OfferingUpdateRequest request, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        validateIsProposer(offering, member);
        UpdatedOffering updatedOffering = request.toUpdatedOffering();
        validateUpdatedTotalCount(offering.getCurrentCount(), updatedOffering.getOfferingPrice().getTotalCount());
        offering.update(updatedOffering);
        updateStatus(offering);
        return new OfferingUpdateResponse(offering, offering.toOfferingPrice(), offering.toOfferingJoinedCount());
    }

    private void validateUpdatedTotalCount(Integer currentCount, Integer updatedTotalCount) {
        if (updatedTotalCount < currentCount) {
            throw new MarketException(OfferingErrorCode.CANNOT_UPDATE_LESS_THAN_CURRENT_COUNT);
        }
    }

    private void updateStatus(OfferingEntity offering) { // TODO : 도메인 분리 필요
        OfferingStatus offeringStatus = offering.toOfferingJoinedCount().decideOfferingStatus();
        LocalDate tomorrow = LocalDate.now(clock).plusDays(1);
        LocalDate meetingDate = offering.getMeetingDate().toLocalDate();
        if (meetingDate.isBefore(tomorrow) || meetingDate.isEqual(tomorrow)) {
            offeringStatus = OfferingStatus.IMMINENT;
        }
        offering.updateOfferingStatus(offeringStatus);
    }

    @WriterDatabase
    @Transactional
    public void deleteOffering(Long offeringId, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        validateIsProposer(offering, member);
        validateInProgress(offering);
        offeringRepository.delete(offering);
        eventPublisher.publishEvent(new DeleteOfferingEvent(this, offering));
    }

    private void validateInProgress(OfferingEntity offering) {
        if (offering.getRoomStatus().isInProgress()) {
            throw new MarketException(OfferingErrorCode.CANNOT_DELETE_STATUS);
        }
    }

    public OfferingMetaResponse getOfferingMetaInfo(Long offeringId) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        return new OfferingMetaResponse(offering, storageService.getResourceHost());
    }
}
