package com.zzang.chongdae.offering.repository;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface OfferingRepository extends JpaRepository<OfferingEntity, Long> {

    @Query(value = """
            SELECT *
            FROM offering as o
            WHERE o.id = :offeringId
            """, nativeQuery = true)
    Optional<OfferingEntity> findByIdWithDeleted(Long offeringId);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE o.id < :lastId
            ORDER BY o.id DESC
            """)
    List<OfferingEntity> findRecentOfferingsWithoutKeyword(Long lastId, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE o.id < :lastId
                AND (o.title LIKE :keyword% OR o.meetingAddress LIKE :keyword%)
            ORDER BY o.id DESC
            """)
    List<OfferingEntity> findRecentOfferingsWithKeyword(Long lastId, String keyword, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.meetingDate > :lastMeetingDate OR (o.meetingDate = :lastMeetingDate AND o.id < :lastId))
                AND (o.offeringStatus = 'IMMINENT')
            ORDER BY o.meetingDate ASC, o.id DESC
            """)
    List<OfferingEntity> findImminentOfferingsWithoutKeyword(
            LocalDateTime lastMeetingDate, Long lastId, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.meetingAddress LIKE :keyword%)
                AND (o.offeringStatus = 'IMMINENT')
                AND (o.meetingDate > :lastMeetingDate OR (o.meetingDate = :lastMeetingDate AND o.id < :lastId))
            ORDER BY o.meetingDate ASC, o.id DESC
            """)
    List<OfferingEntity> findImminentOfferingsWithMeetingAddressKeyword(
            LocalDateTime lastMeetingDate, Long lastId, String keyword, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.title LIKE :keyword%)
                AND (o.offeringStatus = 'IMMINENT')
                AND (o.meetingDate > :lastMeetingDate OR (o.meetingDate = :lastMeetingDate AND o.id < :lastId))
            ORDER BY o.meetingDate ASC, o.id DESC
            """)
    List<OfferingEntity> findImminentOfferingsWithTitleKeyword(
            LocalDateTime lastMeetingDate, Long lastId, String keyword, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE ((o.discountRate < :lastDiscountRate) or (o.discountRate = :lastDiscountRate AND o.id < :lastId))
                AND (o.offeringStatus IN ('AVAILABLE', 'FULL', 'IMMINENT'))
            ORDER BY o.discountRate DESC, o.id DESC
            """)
    List<OfferingEntity> findHighDiscountOfferingsWithoutKeyword(
            double lastDiscountRate, Long lastId, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.title LIKE :keyword%)
               AND (o.offeringStatus IN ('AVAILABLE', 'FULL', 'IMMINENT'))
               AND ((o.discountRate < :lastDiscountRate) or (o.discountRate = :lastDiscountRate AND o.id < :lastId))
            ORDER BY o.discountRate DESC, o.id DESC
            """)
    List<OfferingEntity> findHighDiscountOfferingsWithTitleKeyword(
            double lastDiscountRate, Long lastId, String keyword, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.meetingAddress LIKE :keyword%)
               AND (o.offeringStatus IN ('AVAILABLE', 'FULL', 'IMMINENT'))
               AND ((o.discountRate < :lastDiscountRate) or (o.discountRate = :lastDiscountRate AND o.id < :lastId))
            ORDER BY o.discountRate DESC, o.id DESC
            """)
    List<OfferingEntity> findHighDiscountOfferingsWithMeetingAddressKeyword(
            double lastDiscountRate, Long lastId, String keyword, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.id < :lastId)
                AND (o.offeringStatus IN ('AVAILABLE', 'IMMINENT'))
            ORDER BY o.id DESC
            """)
    List<OfferingEntity> findJoinableOfferingsWithoutKeyword(Long lastId, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.id < :lastId)
               AND (o.title LIKE :keyword% OR o.meetingAddress LIKE :keyword%)
               AND (o.offeringStatus IN ('AVAILABLE', 'IMMINENT'))
            ORDER BY o.id DESC
            """)
    List<OfferingEntity> findJoinableOfferingsWithKeyword(Long lastId, String keyword, Pageable pageable);

    @Query("SELECT MAX(o.id) FROM OfferingEntity o")
    Long findMaxId();

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE o.meetingDate = :meetingDate
                AND (o.offeringStatus IN ('AVAILABLE', 'FULL', 'IMMINENT'))
            """)
    List<OfferingEntity> findByMeetingDateAndOfferingStatusNotConfirmed(LocalDateTime meetingDate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM OfferingEntity o WHERE o.id = :id")
    Optional<OfferingEntity> findByIdWithLock(Long id);
}
