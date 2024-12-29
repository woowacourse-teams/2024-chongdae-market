package com.zzang.chongdae.offering.repository;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OfferingCustomRepository {

    List<OfferingEntity> findRecentOfferings(Long lastId, String keyword, Pageable pageable);
}
