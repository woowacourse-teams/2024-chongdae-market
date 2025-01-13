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
    protected List<OfferingEntity> fetchWithoutLast(Long outOfRangeId, String searchKeyword, Pageable pageable) {
        return offeringRepository.findRecentOfferings(outOfRangeId, searchKeyword, pageable);
    }

    @Override
    protected List<OfferingEntity> fetchWithLast(OfferingEntity lastOffering, String searchKeyword, Pageable pageable) {
        Long lastOfferingId = lastOffering.getId();
        return offeringRepository.findRecentOfferings(lastOfferingId, searchKeyword, pageable);
    }
}
