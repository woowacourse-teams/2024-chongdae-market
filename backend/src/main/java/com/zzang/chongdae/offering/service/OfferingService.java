package com.zzang.chongdae.offering.service;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.exception.MemberErrorCode;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.OfferingFilter;
import com.zzang.chongdae.offering.domain.OfferingPrice;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponse;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponseItem;
import com.zzang.chongdae.offering.service.dto.OfferingDetailResponse;
import com.zzang.chongdae.offering.service.dto.OfferingFilterAllResponse;
import com.zzang.chongdae.offering.service.dto.OfferingFilterAllResponseItem;
import com.zzang.chongdae.offering.service.dto.OfferingMeetingResponse;
import com.zzang.chongdae.offering.service.dto.OfferingProductImageRequest;
import com.zzang.chongdae.offering.service.dto.OfferingProductImageResponse;
import com.zzang.chongdae.offering.service.dto.OfferingSaveRequest;
import com.zzang.chongdae.offeringmember.repository.OfferingMemberRepository;
import com.zzang.chongdae.storage.service.StorageService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class OfferingService {

    private static final Long OUT_OF_RANGE_ID_OFFSET = 1L;

    private final OfferingRepository offeringRepository;
    private final OfferingMemberRepository offeringMemberRepository;
    private final MemberRepository memberRepository;
    private final StorageService storageService;
    private final ProductImageExtractor extractor;

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

    public OfferingAllResponse getAllOffering(String filterName, String searchKeyword, Long lastId, Integer pageSize) {
        Pageable pageable = PageRequest.ofSize(pageSize);
        OfferingFilter filter = OfferingFilter.findByName(filterName);
        List<OfferingEntity> offerings = fetchOfferings(filter, searchKeyword, lastId, pageable);
        return new OfferingAllResponse(offerings.stream()
                .map(offering -> {
                    OfferingPrice offeringPrice = offering.toOfferingPrice();
                    OfferingStatus offeringStatus = offering.toOfferingStatus();
                    return new OfferingAllResponseItem(offering, offeringPrice, offeringStatus);
                })
                .toList()
        );
    }

    private List<OfferingEntity> fetchOfferings(
            OfferingFilter filter, String searchKeyword, Long lastId, Pageable pageable) {
        if (filter == OfferingFilter.RECENT) {
            return fetchRecentOfferings(searchKeyword, lastId, pageable);
        }
        if (filter == OfferingFilter.IMMINENT) {
            return fetchImminentOfferings(searchKeyword, lastId, pageable);
        }
        if (filter == OfferingFilter.HIGH_DISCOUNT) {
            return fetchHighDiscountOfferings(searchKeyword, lastId, pageable);
        }
        throw new MarketException(OfferingErrorCode.NOT_SUPPORTED_FILTER);
    }

    private List<OfferingEntity> fetchRecentOfferings(String searchKeyword, Long lastId, Pageable pageable) {
        if (lastId == null) {
            Long outOfRangeId = findOutOfRangeId();
            return offeringRepository.findRecentOfferingsWithKeyword(outOfRangeId, searchKeyword, pageable);
        }
        OfferingEntity lastOffering = offeringRepository.findById(lastId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        return offeringRepository.findRecentOfferingsWithKeyword(lastOffering.getId(), searchKeyword, pageable);
    }

    private List<OfferingEntity> fetchImminentOfferings(String searchKeyword, Long lastId, Pageable pageable) {
        if (lastId == null) {
            LocalDateTime outOfRangeDeadline = LocalDateTime.now();
            Long outOfRangeId = findOutOfRangeId();
            return offeringRepository.findImminentOfferingsWithKeyword(
                    outOfRangeDeadline, outOfRangeId, searchKeyword, pageable);
        }
        OfferingEntity lastOffering = offeringRepository.findById(lastId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        return offeringRepository.findImminentOfferingsWithKeyword(
                lastOffering.getDeadline(), lastOffering.getId(), searchKeyword, pageable);
    }

    private List<OfferingEntity> fetchHighDiscountOfferings(String searchKeyword, Long lastId, Pageable pageable) {
        if (lastId == null) {
            double outOfRangeDiscountRate = 1;
            Long outOfRangeId = findOutOfRangeId();
            return offeringRepository.findHighDiscountOfferingsWithKeyword(
                    outOfRangeDiscountRate, outOfRangeId, searchKeyword, pageable);
        }
        OfferingEntity lastOffering = offeringRepository.findById(lastId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        double discountRate = (double) (lastOffering.getEachPrice()
                - lastOffering.getTotalPrice() / lastOffering.getTotalCount())
                / lastOffering.getEachPrice(); // TODO: 도메인으로 분리
        return offeringRepository.findHighDiscountOfferingsWithKeyword(discountRate, lastOffering.getId(),
                searchKeyword, pageable);
    }

    private Long findOutOfRangeId() {
        return offeringRepository.findMaxId() + OUT_OF_RANGE_ID_OFFSET;
    }

    public OfferingMeetingResponse getOfferingMeeting(Long offeringId) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        return new OfferingMeetingResponse(offering.toOfferingMeeting());
    }

    public OfferingFilterAllResponse getAllOfferingFilter() {
        List<OfferingFilterAllResponseItem> filters = Arrays.stream(OfferingFilter.values())
                .map(OfferingFilterAllResponseItem::new)
                .toList();
        return new OfferingFilterAllResponse(filters);
    }

    public Long saveOffering(OfferingSaveRequest request) {
        MemberEntity member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new MarketException(MemberErrorCode.NOT_FOUND));
        OfferingEntity offering = request.toEntity(member);
        OfferingEntity savedOffering = offeringRepository.save(offering);
        return savedOffering.getId();
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
