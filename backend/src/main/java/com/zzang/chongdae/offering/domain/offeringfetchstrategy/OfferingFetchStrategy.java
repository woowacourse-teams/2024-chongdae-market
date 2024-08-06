package com.zzang.chongdae.offering.domain.offeringfetchstrategy;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public abstract class OfferingFetchStrategy {

    private static final Long OUT_OF_RANGE_ID_OFFSET = 1L;

    protected final OfferingRepository offeringRepository;

    protected Long findOutOfRangeId() {
        return offeringRepository.findMaxId() + OUT_OF_RANGE_ID_OFFSET;
    }

    public List<OfferingEntity> fetchOfferings(String searchKeyword, Long lastId, Pageable pageable) {
        if (lastId == null) {
            return fetchOfferingsWithoutLastId(searchKeyword, pageable);
        }
        OfferingEntity lastOffering = offeringRepository.findById(lastId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        return fetchOfferingsWithLastOffering(lastOffering, searchKeyword, pageable);
    }

    protected abstract List<OfferingEntity> fetchOfferingsWithoutLastId(String searchKeyword, Pageable pageable);

    protected abstract List<OfferingEntity> fetchOfferingsWithLastOffering(
            OfferingEntity lastOffering, String searchKeyword, Pageable pageable);
}
