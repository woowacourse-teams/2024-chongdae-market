package com.zzang.chongdae.offering.domain.offeringfetchstrategy;

import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.Comparator;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class HighDiscountOfferingStrategy extends OfferingFetchStrategy {

    public HighDiscountOfferingStrategy(OfferingRepository offeringRepository) {
        super(offeringRepository);
    }

    @Override
    protected List<OfferingEntity> fetchOfferingsWithoutLastId(String searchKeyword, Pageable pageable) {
        double outOfRangeDiscountRate = 100;
        Long outOfRangeId = findOutOfRangeId();
        return fetchOfferings(searchKeyword, pageable, outOfRangeDiscountRate, outOfRangeId);
    }

    @Override
    protected List<OfferingEntity> fetchOfferingsWithLastOffering(
            OfferingEntity lastOffering, String searchKeyword, Pageable pageable) {
        Double lastDiscountRate = lastOffering.getDiscountRate();
        Long lastId = lastOffering.getId();
        return fetchOfferings(searchKeyword, pageable, lastDiscountRate, lastId);
    }

    private List<OfferingEntity> fetchOfferings(String searchKeyword, Pageable pageable,
                                                double lastDiscountRate, Long lastId) {
        if (searchKeyword == null) {
            return offeringRepository.findHighDiscountOfferingsWithoutKeyword(
                    lastDiscountRate, lastId, pageable);
        }
        Comparator<OfferingEntity> sortCondition = Comparator
                .comparing(OfferingEntity::getDiscountRate)
                .thenComparing(OfferingEntity::getId, Comparator.reverseOrder());
        return concatOfferings(pageable, sortCondition,
                offeringRepository.findHighDiscountOfferingsWithTitleKeyword(
                        lastDiscountRate,
                        lastId,
                        searchKeyword,
                        pageable),
                offeringRepository.findHighDiscountOfferingsWithMeetingAddressKeyword(
                        lastDiscountRate,
                        lastId,
                        searchKeyword,
                        pageable));
    }
}
