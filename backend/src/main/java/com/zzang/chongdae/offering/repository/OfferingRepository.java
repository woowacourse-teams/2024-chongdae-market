package com.zzang.chongdae.offering.repository;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OfferingRepository extends JpaRepository<OfferingEntity, Long>, CustomizedOfferingRepository {

    @Query(value = """
            SELECT *
            FROM offering as o
            WHERE o.id = :offeringId
            """, nativeQuery = true)
    Optional<OfferingEntity> findByIdWithDeleted(Long offeringId);

    @Query("SELECT MAX(o.id) FROM OfferingEntity o")
    Long findMaxId();

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE o.meetingDate = :meetingDate
                AND (o.offeringStatus IN ('AVAILABLE', 'FULL', 'IMMINENT'))
            """)
    List<OfferingEntity> findByMeetingDateAndOfferingStatusNotConfirmed(LocalDateTime meetingDate);
}
