package com.zzang.chongdae.offering.repository;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferingRepository extends JpaRepository<OfferingEntity, Long> {
    List<OfferingEntity> findByIdGreaterThan(Long lastId, Pageable pageable);
}
