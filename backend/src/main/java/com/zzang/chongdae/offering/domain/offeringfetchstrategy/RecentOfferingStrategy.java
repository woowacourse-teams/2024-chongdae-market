package com.zzang.chongdae.offering.domain.offeringfetchstrategy;

import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class RecentOfferingStrategy extends OfferingFetchStrategy {

    public RecentOfferingStrategy(OfferingRepository offeringRepository) {
        super(offeringRepository);
    }

    @Override
    protected List<OfferingEntity> fetchOfferingsWithoutLastId(String searchKeyword, Pageable pageable) {
        Long outOfRangeId = findOutOfRangeId();
        if (searchKeyword == null) {
            return offeringRepository.findRecentOfferingsWithoutKeyword(outOfRangeId, pageable);
        }
        List<OfferingEntity> offeringsWithTitleKeyword = offeringRepository.findRecentOfferingsWithTitleKeyword(
                outOfRangeId, searchKeyword, pageable);
        List<OfferingEntity> offeringsWithAddressKeyword = offeringRepository.findRecentOfferingsWithMeetingAddressKeyword(
                outOfRangeId, searchKeyword, pageable);
        return of(offeringsWithTitleKeyword, offeringsWithAddressKeyword);
    }

    @Override
    protected List<OfferingEntity> fetchOfferingsWithLastOffering(
            OfferingEntity lastOffering, String searchKeyword, Pageable pageable) {
        if (searchKeyword == null) {
            return offeringRepository.findRecentOfferingsWithoutKeyword(lastOffering.getId(), pageable);
        }
        List<OfferingEntity> offeringsWithTitleKeyword = offeringRepository.findRecentOfferingsWithTitleKeyword(
                lastOffering.getId(), searchKeyword, pageable);
        List<OfferingEntity> offeringsWithAddressKeyword = offeringRepository.findRecentOfferingsWithMeetingAddressKeyword(
                lastOffering.getId(), searchKeyword, pageable);
        return of(offeringsWithTitleKeyword, offeringsWithAddressKeyword);
    }

    private List<OfferingEntity> of(List<OfferingEntity> firstOffering, List<OfferingEntity> secondOffering) {
        List<OfferingEntity> result = new ArrayList<>();
        result.addAll(firstOffering);
        result.addAll(secondOffering);
        return result;
    }
}
