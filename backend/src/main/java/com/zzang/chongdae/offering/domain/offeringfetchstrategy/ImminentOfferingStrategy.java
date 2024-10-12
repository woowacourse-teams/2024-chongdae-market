package com.zzang.chongdae.offering.domain.offeringfetchstrategy;

import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
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
            return offeringRepository.findImminentOfferingsWithoutKeyword(lastMeetingDate, lastId, pageable);
        }
        return Stream.concat(
                        offeringRepository.findImminentOfferingsWithMeetingAddressKeyword(lastMeetingDate, lastId,
                                searchKeyword,
                                pageable).stream(),
                        offeringRepository.findImminentOfferingsWithTitleKeyword(lastMeetingDate, lastId, searchKeyword,
                                pageable).stream()
                ).sorted(Comparator.comparing(OfferingEntity::getMeetingDate)
                        .thenComparing(OfferingEntity::getId, Comparator.reverseOrder()))
                .limit(pageable.getPageSize())
                .toList();
    }
}
