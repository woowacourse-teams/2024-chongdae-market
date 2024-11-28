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
    protected List<OfferingEntity> fetchOfferingsWithoutLastId(String searchKeyword, Pageable pageable) {
        Long outOfRangeId = findOutOfRangeId();
        return offeringRepository.findRecentOfferingsWithKeyword(outOfRangeId, searchKeyword, pageable);
    }

    @Override
    protected List<OfferingEntity> fetchOfferingsWithLastOffering(
            OfferingEntity lastOffering, String searchKeyword, Pageable pageable) {
        return offeringRepository.findRecentOfferingsWithKeyword(lastOffering.getId(), searchKeyword, pageable);
    }
}
