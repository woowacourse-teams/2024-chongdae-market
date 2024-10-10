package com.zzang.chongdae.offering.domain.offeringfetchstrategy;

import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class ImminentOfferingStrategy extends OfferingFetchStrategy { // TODO: transactional

    public ImminentOfferingStrategy(OfferingRepository offeringRepository) {
        super(offeringRepository);
    }

    @Override
    protected List<OfferingEntity> fetchOfferingsWithoutLastId(String searchKeyword, Pageable pageable) {
        LocalDateTime outOfRangeMeetingDate = LocalDateTime.now();
        Long outOfRangeId = findOutOfRangeId();
        return fetchOfferings(searchKeyword, pageable, outOfRangeMeetingDate, outOfRangeId);
    }

    @Override
    protected List<OfferingEntity> fetchOfferingsWithLastOffering(
            OfferingEntity lastOffering, String searchKeyword, Pageable pageable) {
        LocalDateTime lastMeetingDate = lastOffering.getMeetingDate();
        Long lastId = lastOffering.getId();
        return fetchOfferings(searchKeyword, pageable, lastMeetingDate, lastId);
    }

    private List<OfferingEntity> fetchOfferings(String searchKeyword, Pageable pageable,
                                                LocalDateTime lastMeetingDate, Long lastId) { // todo: queryDsl
        if (searchKeyword == null) {
            return of(offeringRepository.findImminentOfferingsWithoutKeywordMoreThanMeetingDate(lastMeetingDate,
                            pageable),
                    offeringRepository.findImminentOfferingsWithoutKeywordEqualMeetingDate(lastMeetingDate, lastId,
                            pageable));
        }
        List<OfferingEntity> result = of(
                offeringRepository.findImminentOfferingsWithTitleKeywordMoreThanMeetingDate(lastMeetingDate,
                        searchKeyword, pageable),
                offeringRepository.findImminentOfferingsWithMeetingAddressKeywordMoreMeetingDate(lastMeetingDate,
                        searchKeyword, pageable),
                offeringRepository.findImminentOfferingsWithTitleKeywordEqualMeetingDate(lastMeetingDate, lastId,
                        searchKeyword, pageable),
                offeringRepository.findImminentOfferingsWithMeetingAddressKeywordEqualMeetingDate(lastMeetingDate,
                        lastId,
                        searchKeyword, pageable));
        result.sort(Comparator.comparing(OfferingEntity::getMeetingDate)
                .thenComparing(OfferingEntity::getId, Comparator.reverseOrder()));
        return result;
    }

    private List<OfferingEntity> of(List<OfferingEntity>... offerings) {
        List<OfferingEntity> result = new ArrayList<>();
        for (List<OfferingEntity> offering : offerings) {
            result.addAll(offering);
        } // todo: 정렬 로직 추가
        return result;
    }
}
