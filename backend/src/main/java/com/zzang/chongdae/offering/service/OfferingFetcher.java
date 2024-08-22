package com.zzang.chongdae.offering.service;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.offering.domain.OfferingFilter;
import com.zzang.chongdae.offering.domain.offeringfetchstrategy.HighDiscountOfferingStrategy;
import com.zzang.chongdae.offering.domain.offeringfetchstrategy.ImminentOfferingStrategy;
import com.zzang.chongdae.offering.domain.offeringfetchstrategy.JoinableOfferingStrategy;
import com.zzang.chongdae.offering.domain.offeringfetchstrategy.OfferingFetchStrategy;
import com.zzang.chongdae.offering.domain.offeringfetchstrategy.RecentOfferingStrategy;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OfferingFetcher {

    private final Map<OfferingFilter, OfferingFetchStrategy> strategyMap;

    @Autowired
    public OfferingFetcher(OfferingRepository offeringRepository) {
        this.strategyMap = Map.of(
                OfferingFilter.RECENT, new RecentOfferingStrategy(offeringRepository),
                OfferingFilter.IMMINENT, new ImminentOfferingStrategy(offeringRepository),
                OfferingFilter.HIGH_DISCOUNT, new HighDiscountOfferingStrategy(offeringRepository),
                OfferingFilter.JOINABLE, new JoinableOfferingStrategy(offeringRepository)
        );
    }

    public List<OfferingEntity> fetchOfferings(
            OfferingFilter filter, String searchKeyword, Long lastId, Pageable pageable) {
        OfferingFetchStrategy strategy = strategyMap.get(filter);
        if (strategy == null) {
            throw new MarketException(OfferingErrorCode.NOT_SUPPORTED_FILTER);
        }
        return strategy.fetchOfferings(searchKeyword, lastId, pageable);
    }
}
