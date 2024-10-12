package com.zzang.chongdae.offering.domain.offeringfetchstrategy;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public abstract class OfferingFetchStrategy {

    private static final Long OUT_OF_RANGE_ID_OFFSET = 1L;

    protected final OfferingRepository offeringRepository;

    protected Long findOutOfRangeId() {
        return Optional.ofNullable(offeringRepository.findMaxId())
                .orElse(0L) + OUT_OF_RANGE_ID_OFFSET;
    }

    public List<OfferingEntity> fetchOfferings(String searchKeyword, Long lastId, Pageable pageable) {
        if (lastId == null) {
            return fetchOfferingsWithoutLastId(searchKeyword, pageable);
        }
        OfferingEntity lastOffering = offeringRepository.findById(lastId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        return fetchOfferingsWithLastOffering(lastOffering, searchKeyword, pageable);
    }

    protected List<OfferingEntity> concatOfferings(Pageable pageable,
                                                   Comparator<OfferingEntity> sortCondition,
                                                   List<OfferingEntity>... offerings) {
        return Stream.of(offerings)
                .flatMap(Collection::stream)
                .sorted(sortCondition)
                .limit(pageable.getPageSize())
                .toList();
    }

    // todo: 합치기
    protected abstract List<OfferingEntity> fetchOfferingsWithoutLastId(String searchKeyword, Pageable pageable);

    protected abstract List<OfferingEntity> fetchOfferingsWithLastOffering(
            OfferingEntity lastOffering, String searchKeyword, Pageable pageable);
}
