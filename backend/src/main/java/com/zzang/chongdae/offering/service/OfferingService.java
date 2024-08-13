package com.zzang.chongdae.offering.service;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.domain.OfferingFilter;
import com.zzang.chongdae.offering.domain.OfferingMeeting;
import com.zzang.chongdae.offering.domain.OfferingPrice;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offering.service.dto.CommentRoomStatusResponse;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponse;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponseItem;
import com.zzang.chongdae.offering.service.dto.OfferingDetailResponse;
import com.zzang.chongdae.offering.service.dto.OfferingFilterAllResponse;
import com.zzang.chongdae.offering.service.dto.OfferingFilterAllResponseItem;
import com.zzang.chongdae.offering.service.dto.OfferingMeetingResponse;
import com.zzang.chongdae.offering.service.dto.OfferingMeetingUpdateRequest;
import com.zzang.chongdae.offering.service.dto.OfferingProductImageRequest;
import com.zzang.chongdae.offering.service.dto.OfferingProductImageResponse;
import com.zzang.chongdae.offering.service.dto.OfferingSaveRequest;
import com.zzang.chongdae.offering.service.dto.OfferingStatusResponse;
import com.zzang.chongdae.offeringmember.domain.OfferingMemberRole;
import com.zzang.chongdae.offeringmember.repository.OfferingMemberRepository;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import com.zzang.chongdae.storage.service.StorageService;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class OfferingService {

    private final OfferingRepository offeringRepository;
    private final OfferingMemberRepository offeringMemberRepository;
    private final StorageService storageService;
    private final ProductImageExtractor extractor;
    private final OfferingFetcher offeringFetcher;

    public OfferingDetailResponse getOfferingDetail(Long offeringId, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));

        OfferingPrice offeringPrice = offering.toOfferingPrice();
        OfferingStatus offeringStatus = offering.toOfferingStatus();

        Boolean isParticipated = offeringMemberRepository.existsByOfferingAndMember(offering, member);

        return new OfferingDetailResponse(offering, offeringPrice, offeringStatus, isParticipated);
    }

    public OfferingAllResponse getAllOffering(String filterName, String searchKeyword, Long lastId, Integer pageSize) {
        Pageable pageable = PageRequest.ofSize(pageSize);
        OfferingFilter filter = OfferingFilter.findByName(filterName);
        List<OfferingEntity> offerings = offeringFetcher.fetchOfferings(filter, searchKeyword, lastId, pageable);
        return new OfferingAllResponse(offerings.stream()
                .map(offering -> {
                    OfferingPrice offeringPrice = offering.toOfferingPrice();
                    OfferingStatus offeringStatus = offering.toOfferingStatus();
                    return new OfferingAllResponseItem(offering, offeringPrice, offeringStatus);
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
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        validateIsParticipant(member, offering);
        return new OfferingMeetingResponse(offering.toOfferingMeeting());
    }

    private void validateIsParticipant(MemberEntity member, OfferingEntity offering) {
        if (!offeringMemberRepository.existsByOfferingAndMember(offering, member)) {
            throw new MarketException(OfferingErrorCode.NOT_PARTICIPATE_MEMBER);
        }
    }

    @Transactional
    public OfferingMeetingResponse updateOfferingMeeting(
            Long offeringId, OfferingMeetingUpdateRequest request, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        validateIsProposer(offering, member);
        OfferingMeeting offeringMeeting = request.toOfferingMeeting();
        offering.updateMeeting(offeringMeeting);
        return new OfferingMeetingResponse(offering.toOfferingMeeting());
    }

    private void validateIsProposer(OfferingEntity offering, MemberEntity member) {
        if (offering.isNotProposedBy(member)) {
            throw new MarketException(OfferingErrorCode.NOT_PROPOSE_MEMBER);
        }
    }

    public Long saveOffering(OfferingSaveRequest request, MemberEntity member) {
        OfferingEntity offering = request.toEntity(member);
        OfferingPrice offeringPrice = offering.toOfferingPrice();
        offeringPrice.validateOriginPrice();
        OfferingEntity savedOffering = offeringRepository.save(offering);

        OfferingMemberEntity offeringMember = new OfferingMemberEntity(member, offering, OfferingMemberRole.PROPOSER);
        offeringMemberRepository.save(offeringMember);

        return savedOffering.getId();
    }

    public OfferingStatusResponse getOfferingStatus(Long offeringId) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        if (offering.isStatusGrouping() && offering.toOfferingStatus().isAutoConfirmed()) {
            offering.moveStatus();
        }
        return new OfferingStatusResponse(offering.getRoomStatus());
    }

    @Transactional
    public CommentRoomStatusResponse updateCommentRoomStatus(Long offeringId, MemberEntity member) {
        // TODO: loginMember 가 총대 권한을 가지고 있는지 확인
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        CommentRoomStatus updatedStatus = offering.moveStatus();
        if (updatedStatus.equals(CommentRoomStatus.BUYING)) {
            offering.manuallyConfirm();
        }
        return new CommentRoomStatusResponse(updatedStatus);
    }

    public OfferingProductImageResponse uploadProductImage(MultipartFile image) {
        String imageUrl = storageService.uploadFile(image, "chongdae-market/images/offerings/product/");
        return new OfferingProductImageResponse(imageUrl);
    }

    public OfferingProductImageResponse extractProductImage(OfferingProductImageRequest request) {
        String imageUrl = extractor.extract(request.productUrl());
        return new OfferingProductImageResponse(imageUrl);
    }
}