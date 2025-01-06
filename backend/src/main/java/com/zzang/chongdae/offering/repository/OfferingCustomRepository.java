package com.zzang.chongdae.offering.repository;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OfferingCustomRepository {

    List<OfferingEntity> findRecentOfferings(Long lastId, String keyword, Pageable pageable);

    List<OfferingEntity> findImminentOfferingsWithTitleKeyword(
            LocalDateTime lastMeetingDate, Long lastId, String keyword, Pageable pageable);

    List<OfferingEntity> findImminentOfferingsWithMeetingAddressKeyword(
            LocalDateTime lastMeetingDate, Long lastId, String keyword, Pageable pageable);

    List<OfferingEntity> findHighDiscountOfferingsWithTitleKeyword(
            double lastDiscountRate, Long lastId, String keyword, Pageable pageable);

    List<OfferingEntity> findHighDiscountOfferingsWithMeetingAddressKeyword(
            double lastDiscountRate, Long lastId, String keyword, Pageable pageable);

    List<OfferingEntity> findJoinableOfferings(Long lastId, String keyword, Pageable pageable);
}
