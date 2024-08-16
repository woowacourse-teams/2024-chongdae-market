package com.zzang.chongdae.offering.domain.offeringfetchstrategy;

import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class HighDiscountOfferingStrategy extends OfferingFetchStrategy {

    public HighDiscountOfferingStrategy(OfferingRepository offeringRepository) {
        super(offeringRepository);
    }

    @Override
    protected List<OfferingEntity> fetchOfferingsWithoutLastId(String searchKeyword, Pageable pageable) {
        double outOfRangeDiscountRate = 1;
        Long outOfRangeId = findOutOfRangeId();
        return offeringRepository.findHighDiscountOfferingsWithKeyword(
                outOfRangeDiscountRate, outOfRangeId, searchKeyword, pageable);
    }

    @Override
    protected List<OfferingEntity> fetchOfferingsWithLastOffering(
            OfferingEntity lastOffering, String searchKeyword, Pageable pageable) {
        double lastDiscountRate = lastOffering.toOfferingPrice().calculateDiscountRate();
        return offeringRepository.findHighDiscountOfferingsWithKeyword(
                lastDiscountRate, lastOffering.getId(), searchKeyword, pageable);
    }
}
