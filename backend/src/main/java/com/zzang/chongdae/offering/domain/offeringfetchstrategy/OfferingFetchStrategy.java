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

    public List<OfferingEntity> fetch(String searchKeyword, Long lastId, Pageable pageable) {
        if (lastId == null) {
            return fetchWithoutLast(outOfRangeId(), searchKeyword, pageable);
        }
        return fetchWithLast(lastOffering(lastId), searchKeyword, pageable);
    }

    private Long outOfRangeId() {
        Long maxId = offeringRepository.findMaxId();
        return Optional.ofNullable(maxId).orElse(0L) + OUT_OF_RANGE_ID_OFFSET;
    }

    private OfferingEntity lastOffering(Long lastId) {
        return offeringRepository.findById(lastId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
    }

    protected List<OfferingEntity> concat(Pageable pageable,
                                          Comparator<OfferingEntity> sortCondition,
                                          List<OfferingEntity>... offerings) {
        return Stream.of(offerings)
                .flatMap(Collection::stream)
                .distinct()
                .sorted(sortCondition)
                .limit(pageable.getPageSize())
                .toList();
    }

    protected abstract List<OfferingEntity> fetchWithoutLast(Long outOfRangeId,
                                                             String searchKeyword,
                                                             Pageable pageable);

    protected abstract List<OfferingEntity> fetchWithLast(OfferingEntity lastOffering,
                                                          String searchKeyword,
                                                          Pageable pageable);
}
