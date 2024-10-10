package com.zzang.chongdae.offering.domain.offeringfetchstrategy;

import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.ArrayList;
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
                                                double outOfRangeDiscountRate, Long outOfRangeId) {
        if (searchKeyword == null) {
            return of(offeringRepository.findHighDiscountOfferingsWithoutKeywordLessThanDiscountRate(
                            outOfRangeDiscountRate, pageable),
                    offeringRepository.findHighDiscountOfferingsWithoutKeywordEqualDiscountRate(outOfRangeDiscountRate,
                            outOfRangeId, pageable));
        }
        return of(
                offeringRepository.findHighDiscountOfferingsWithTitleKeywordLessThanDiscountRate(outOfRangeDiscountRate,
                        searchKeyword, pageable),
                offeringRepository.findHighDiscountOfferingsWithTitleKeywordEqualDiscountRate(outOfRangeDiscountRate,
                        outOfRangeId, searchKeyword, pageable),
                offeringRepository.findHighDiscountOfferingsWithMeetingAddressKeywordEqualDiscountRate(
                        outOfRangeDiscountRate, outOfRangeId, searchKeyword, pageable),
                offeringRepository.findHighDiscountOfferingsWithMeetingAddressKeywordLessThanDiscountRate(
                        outOfRangeDiscountRate, searchKeyword, pageable)
        );
    }

    private List<OfferingEntity> of(List<OfferingEntity>... offerings) {
        List<OfferingEntity> result = new ArrayList<>();
        for (List<OfferingEntity> offering : offerings) {
            result.addAll(offering);
        } // todo: 정렬 로직 추가
        return result;
    }
}
