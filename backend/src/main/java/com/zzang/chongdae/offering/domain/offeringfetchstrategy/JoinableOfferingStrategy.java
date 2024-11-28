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
    protected List<OfferingEntity> fetchOfferingsWithoutLastId(String searchKeyword, Pageable pageable) {
        Long outOfRangeId = findOutOfRangeId();
        return offeringRepository.findJoinableOfferingsWithKeyword(outOfRangeId, searchKeyword, pageable);
    }

    @Override
    protected List<OfferingEntity> fetchOfferingsWithLastOffering(OfferingEntity lastOffering, String searchKeyword,
                                                                  Pageable pageable) {
        return offeringRepository.findJoinableOfferingsWithKeyword(lastOffering.getId(), searchKeyword, pageable);
    }
}
