package com.zzang.chongdae.offering.domain.offeringfetchstrategy;

import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class RecentOfferingStrategy extends OfferingFetchStrategy {

    public RecentOfferingStrategy(OfferingRepository offeringRepository) {
        super(offeringRepository);
    }

    @Override
    protected List<OfferingEntity> fetchWithoutLast(String searchKeyword, Pageable pageable) {
        Long outOfRangeId = findOutOfRangeId();
        return fetchOfferings(searchKeyword, pageable, outOfRangeId);
    }

    @Override
    protected List<OfferingEntity> fetchWithLast(
            OfferingEntity lastOffering, String searchKeyword, Pageable pageable) {
        Long lastOfferingId = lastOffering.getId();
        return fetchOfferings(searchKeyword, pageable, lastOfferingId);
    }

    private List<OfferingEntity> fetchOfferings(String searchKeyword, Pageable pageable, Long lastOfferingId) {
        if (searchKeyword == null) {
            return offeringRepository.findRecentOfferingsWithoutKeyword(lastOfferingId, pageable);
        }
        return offeringRepository.findRecentOfferingsWithKeyword(lastOfferingId, searchKeyword, pageable);
    }
}
