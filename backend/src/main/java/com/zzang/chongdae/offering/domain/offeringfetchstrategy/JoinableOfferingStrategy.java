package com.zzang.chongdae.offering.domain.offeringfetchstrategy;

import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class JoinableOfferingStrategy extends OfferingFetchStrategy {

    public JoinableOfferingStrategy(OfferingRepository offeringRepository) {
        super(offeringRepository);
    }

    @Override
    protected List<OfferingEntity> fetchWithoutLast(Long outOfRangeId, String searchKeyword, Pageable pageable) {
        return fetchOfferings(outOfRangeId, searchKeyword, pageable);
    }

    @Override
    protected List<OfferingEntity> fetchWithLast(OfferingEntity lastOffering, String searchKeyword, Pageable pageable) {
        Long lastId = lastOffering.getId();
        return fetchOfferings(lastId, searchKeyword, pageable);
    }

    private List<OfferingEntity> fetchOfferings(Long outOfRangeId, String searchKeyword, Pageable pageable) {
        if (searchKeyword == null) {
            return offeringRepository.findJoinableOfferingsWithoutKeyword(outOfRangeId, pageable);
        }
        return offeringRepository.findJoinableOfferingsWithKeyword(outOfRangeId, searchKeyword, pageable);
    }
}
