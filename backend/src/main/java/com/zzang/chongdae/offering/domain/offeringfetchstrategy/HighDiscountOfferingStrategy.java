package com.zzang.chongdae.offering.domain.offeringfetchstrategy;

import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
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
                                                double outOfRangeDiscountRate, Long outOfRangeId) {
        if (searchKeyword == null) {
            return offeringRepository.findHighDiscountOfferingsWithoutKeyword(
                    outOfRangeDiscountRate, outOfRangeId, pageable);
        }
        return Stream.concat(
                        offeringRepository.findHighDiscountOfferingsWithTitleKeywordLessThanDiscountRate(outOfRangeDiscountRate,
                                outOfRangeId, searchKeyword, pageable).stream(),
                        offeringRepository.findHighDiscountOfferingsWithMeetingAddressKeywordLessThanDiscountRate(
                                outOfRangeDiscountRate, outOfRangeId, searchKeyword, pageable).stream())
                .sorted(Comparator.comparing(OfferingEntity::getDiscountRate)
                        .thenComparing(OfferingEntity::getId, Comparator.reverseOrder()))
                .limit(pageable.getPageSize())
                .toList();
    }
}
