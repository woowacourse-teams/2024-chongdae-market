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
    protected List<OfferingEntity> fetchWithoutLast(Long outOfRangeId, String searchKeyword, Pageable pageable) {
        LocalDateTime outOfRangeMeetingDate = LocalDateTime.now();
        return fetchOfferings(outOfRangeId, outOfRangeMeetingDate, searchKeyword, pageable);
    }

    @Override
    protected List<OfferingEntity> fetchWithLast(OfferingEntity lastOffering, String searchKeyword, Pageable pageable) {
        Long lastId = lastOffering.getId();
        LocalDateTime lastMeetingDate = lastOffering.getMeetingDate();
        return fetchOfferings(lastId, lastMeetingDate, searchKeyword, pageable);
    }

    private List<OfferingEntity> fetchOfferings(Long lastId, LocalDateTime lastMeetingDate,
                                                String searchKeyword, Pageable pageable) {
        if (searchKeyword == null) {
            return offeringRepository.findImminentOfferingsWithoutKeyword(lastMeetingDate, lastId, pageable);
        }
        List<OfferingEntity> offeringsSearchedByTitle = offeringRepository.findImminentOfferingsWithTitleKeyword(
                lastMeetingDate,
                lastId,
                searchKeyword,
                pageable);
        List<OfferingEntity> offeringsSearchedByMeetingAddress = offeringRepository.findImminentOfferingsWithMeetingAddressKeyword(
                lastMeetingDate,
                lastId,
                searchKeyword,
                pageable);
        return concat(pageable, sortCondition(), offeringsSearchedByTitle, offeringsSearchedByMeetingAddress);
    }

    private Comparator<OfferingEntity> sortCondition() {
        return Comparator
                .comparing(OfferingEntity::getMeetingDate)
                .thenComparing(OfferingEntity::getId, Comparator.reverseOrder());
    }
}
