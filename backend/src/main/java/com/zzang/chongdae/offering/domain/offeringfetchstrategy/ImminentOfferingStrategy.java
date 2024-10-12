package com.zzang.chongdae.offering.domain.offeringfetchstrategy;

import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class ImminentOfferingStrategy extends OfferingFetchStrategy {

    public ImminentOfferingStrategy(OfferingRepository offeringRepository) {
        super(offeringRepository);
    }

    @Override
    protected List<OfferingEntity> fetchWithoutLast(String searchKeyword, Pageable pageable) {
        LocalDateTime outOfRangeMeetingDate = LocalDateTime.now();
        Long outOfRangeId = findOutOfRangeId();
        return fetchOfferings(searchKeyword, pageable, outOfRangeMeetingDate, outOfRangeId);
    }

    @Override
    protected List<OfferingEntity> fetchWithLast(
            OfferingEntity lastOffering, String searchKeyword, Pageable pageable) {
        LocalDateTime lastMeetingDate = lastOffering.getMeetingDate();
        Long lastId = lastOffering.getId();
        return fetchOfferings(searchKeyword, pageable, lastMeetingDate, lastId);
    }

    private List<OfferingEntity> fetchOfferings(String searchKeyword, Pageable pageable,
                                                LocalDateTime lastMeetingDate, Long lastId) {
        if (searchKeyword == null) {
            return offeringRepository.findImminentOfferingsWithoutKeyword(lastMeetingDate, lastId, pageable);
        }
        Comparator<OfferingEntity> sortCondition = Comparator
                .comparing(OfferingEntity::getMeetingDate)
                .thenComparing(OfferingEntity::getId, Comparator.reverseOrder());
        return concat(pageable, sortCondition,
                offeringRepository.findImminentOfferingsWithMeetingAddressKeyword(
                        lastMeetingDate,
                        lastId,
                        searchKeyword,
                        pageable),
                offeringRepository.findImminentOfferingsWithTitleKeyword(
                        lastMeetingDate,
                        lastId,
                        searchKeyword,
                        pageable));
    }
}
