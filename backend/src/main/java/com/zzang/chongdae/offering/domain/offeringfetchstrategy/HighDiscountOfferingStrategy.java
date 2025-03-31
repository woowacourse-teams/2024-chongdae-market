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
    protected List<OfferingEntity> fetchWithoutLast(Long outOfRangeId, String searchKeyword, Pageable pageable) {
        double outOfRangeDiscountRate = 100;
        return fetchOfferings(outOfRangeId, outOfRangeDiscountRate, searchKeyword, pageable);
    }

    @Override
    protected List<OfferingEntity> fetchWithLast(OfferingEntity lastOffering, String searchKeyword, Pageable pageable) {
        Long lastId = lastOffering.getId();
        Double lastDiscountRate = lastOffering.getDiscountRate();
        return fetchOfferings(lastId, lastDiscountRate, searchKeyword, pageable);
    }

    private List<OfferingEntity> fetchOfferings(Long lastId, double lastDiscountRate,
                                                String searchKeyword, Pageable pageable) {
        if (searchKeyword == null) {
            return offeringRepository.findHighDiscountOfferingsWithTitleKeyword(
                    lastDiscountRate,
                    lastId,
                    null,
                    pageable);
        }
        List<OfferingEntity> offeringsSearchedByTitle = offeringRepository.findHighDiscountOfferingsWithTitleKeyword(
                lastDiscountRate,
                lastId,
                searchKeyword,
                pageable);
        List<OfferingEntity> offeringsSearchedByMeetingAddress = offeringRepository.findHighDiscountOfferingsWithMeetingAddressKeyword(
                lastDiscountRate,
                lastId,
                searchKeyword,
                pageable);
        return concat(pageable, sortCondition(), offeringsSearchedByTitle, offeringsSearchedByMeetingAddress);
    }

    private Comparator<OfferingEntity> sortCondition() {
        return Comparator
                .comparing(OfferingEntity::getDiscountRate, Comparator.reverseOrder())
                .thenComparing(OfferingEntity::getId, Comparator.reverseOrder());
    }
}
