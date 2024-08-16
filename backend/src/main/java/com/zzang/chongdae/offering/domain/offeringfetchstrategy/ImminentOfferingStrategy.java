package com.zzang.chongdae.offering.domain.offeringfetchstrategy;

import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class ImminentOfferingStrategy extends OfferingFetchStrategy {

    public ImminentOfferingStrategy(OfferingRepository offeringRepository) {
        super(offeringRepository);
    }

    @Override
    protected List<OfferingEntity> fetchOfferingsWithoutLastId(String searchKeyword, Pageable pageable) {
        LocalDateTime outOfRangeMeetingDate = LocalDateTime.now();
        Long outOfRangeId = findOutOfRangeId();
        return offeringRepository.findImminentOfferingsWithKeyword(
                outOfRangeMeetingDate, outOfRangeId, searchKeyword, pageable);
    }

    @Override
    protected List<OfferingEntity> fetchOfferingsWithLastOffering(
            OfferingEntity lastOffering, String searchKeyword, Pageable pageable) {
        LocalDateTime lastMeetingDate = lastOffering.getMeetingDate();
        Long lastId = lastOffering.getId();
        return offeringRepository.findImminentOfferingsWithKeyword(
                lastMeetingDate, lastId, searchKeyword, pageable);
    }
}
