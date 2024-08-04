package com.zzang.chongdae.offering.service;

import static com.zzang.chongdae.offering.repository.OfferingRepository.Specs.greaterThan;
import static com.zzang.chongdae.offering.repository.OfferingRepository.Specs.hasSearchKeyword;
import static org.springframework.data.jpa.domain.Specification.where;

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
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class OfferingService {

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

    public OfferingAllResponse getAllOffering(String filter, String keyword, Long lastId, Integer pageSize) {
        Pageable pageable = PageRequest.ofSize(pageSize);
        Page<OfferingEntity> offerings = offeringRepository.findAll(
                where(hasSearchKeyword(keyword)
                        .and(greaterThan(lastId))
                ),
                pageable
        );
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

    public OfferingFilterAllResponse getOfferingFilter() {
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
