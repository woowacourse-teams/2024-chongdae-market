package com.zzang.chongdae.offering.domain.offeringfetchstrategy;

import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class JoinableOfferingStrategy extends OfferingFetchStrategy {

    public JoinableOfferingStrategy(OfferingRepository offeringRepository) {
        super(offeringRepository);
    }

    @Override
    protected List<OfferingEntity> fetchOfferingsWithoutLastId(String searchKeyword, Pageable pageable) {
        Long outOfRangeId = findOutOfRangeId();
        return fetchOfferings(searchKeyword, pageable, outOfRangeId);
    }

    @Override
    protected List<OfferingEntity> fetchOfferingsWithLastOffering(OfferingEntity lastOffering, String searchKeyword,
                                                                  Pageable pageable) {
        Long lastId = lastOffering.getId();
        return fetchOfferings(searchKeyword, pageable, lastId);
    }

    private List<OfferingEntity> fetchOfferings(String searchKeyword, Pageable pageable, Long outOfRangeId) {
        if (searchKeyword == null) {
            return offeringRepository.findJoinableOfferingsWithoutKeyword(outOfRangeId, pageable);
        }
        return of(offeringRepository.findJoinableOfferingsWithTitleKeyword(outOfRangeId, searchKeyword, pageable),
                offeringRepository.findJoinableOfferingsWithMeetingAddressKeyword(outOfRangeId, searchKeyword,
                        pageable));
    }

    private List<OfferingEntity> of(List<OfferingEntity>... offerings) {
        List<OfferingEntity> result = new ArrayList<>();
        for (List<OfferingEntity> offering : offerings) {
            result.addAll(offering);
        } // todo: 정렬 로직 추가
        return result;
    }
}
