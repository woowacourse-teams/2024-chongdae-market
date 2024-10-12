package com.zzang.chongdae.offering.repository;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OfferingRepository extends JpaRepository<OfferingEntity, Long> {

    @Query("""
            SELECT o
            FROM OfferingEntity as o JOIN OfferingMemberEntity as om
                ON o.id = om.offering.id
            WHERE om.member = :member
            """)
    List<OfferingEntity> findCommentRoomsByMember(MemberEntity member);

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

    // ============================================================

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.meetingDate > :lastMeetingDate OR (o.meetingDate = :lastMeetingDate AND o.id < :lastId))
                AND (o.offeringStatus = 'IMMINENT')
            ORDER BY o.meetingDate ASC, o.id DESC
            """)
    List<OfferingEntity> findImminentOfferingsWithoutKeyword(LocalDateTime lastMeetingDate,
                                                             Long lastId,
                                                             Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.meetingAddress LIKE :keyword%)
                AND (o.offeringStatus = 'IMMINENT')
                AND (o.meetingDate > :lastMeetingDate OR (o.meetingDate = :lastMeetingDate AND o.id < :lastId))
            ORDER BY o.meetingDate ASC, o.id DESC
            """)
    List<OfferingEntity> findImminentOfferingsWithTitleKeyword(
            LocalDateTime lastMeetingDate, Long lastId, String keyword, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.title LIKE :keyword%)
                AND (o.offeringStatus = 'IMMINENT')
                AND (o.meetingDate > :lastMeetingDate OR (o.meetingDate = :lastMeetingDate AND o.id < :lastId))
            ORDER BY o.meetingDate ASC, o.id DESC
            """)
    List<OfferingEntity> findImminentOfferingsWithAddressKeyword(
            LocalDateTime lastMeetingDate, Long lastId, String keyword, Pageable pageable);

    // ============================================================
//    @Query("""
//            SELECT o
//            FROM OfferingEntity o
//            WHERE (o.offeringStatus IN ('AVAILABLE', 'FULL', 'IMMINENT'))
//               AND (o.discountRate < :lastDiscountRate OR (o.discountRate = :lastDiscountRate AND o.id < :lastId))
//               AND (:keyword IS NULL OR o.title LIKE :keyword% OR o.meetingAddress LIKE :keyword%)
//            ORDER BY o.discountRate DESC, o.id DESC
//            """)
//    List<OfferingEntity> findHighDiscountOfferingsWithKeyword(
//            double lastDiscountRate, Long lastId, String keyword, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.discountRate < :lastDiscountRate)
                AND (o.offeringStatus IN ('AVAILABLE', 'FULL', 'IMMINENT'))
            ORDER BY o.discountRate DESC, o.id DESC
            """)
    List<OfferingEntity> findHighDiscountOfferingsWithoutKeywordLessThanDiscountRate(
            double lastDiscountRate, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.discountRate = :lastDiscountRate AND o.id < :lastId)
                AND (o.offeringStatus IN ('AVAILABLE', 'FULL', 'IMMINENT'))
            ORDER BY o.discountRate DESC, o.id DESC
            """)
    List<OfferingEntity> findHighDiscountOfferingsWithoutKeywordEqualDiscountRate(
            double lastDiscountRate, Long lastId, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.discountRate < :lastDiscountRate)
               AND (o.title LIKE :keyword%)
               AND (o.offeringStatus IN ('AVAILABLE', 'FULL', 'IMMINENT'))
            ORDER BY o.discountRate DESC, o.id DESC
            """)
    List<OfferingEntity> findHighDiscountOfferingsWithTitleKeywordLessThanDiscountRate(
            double lastDiscountRate, String keyword, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.discountRate = :lastDiscountRate AND o.id < :lastId)
               AND (o.title LIKE :keyword%)
               AND (o.offeringStatus IN ('AVAILABLE', 'FULL', 'IMMINENT'))
            ORDER BY o.discountRate DESC, o.id DESC
            """)
    List<OfferingEntity> findHighDiscountOfferingsWithTitleKeywordEqualDiscountRate(
            double lastDiscountRate, Long lastId, String keyword, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.discountRate < :lastDiscountRate)
               AND (o.meetingAddress LIKE :keyword%)
               AND (o.offeringStatus IN ('AVAILABLE', 'FULL', 'IMMINENT'))
            ORDER BY o.discountRate DESC, o.id DESC
            """)
    List<OfferingEntity> findHighDiscountOfferingsWithMeetingAddressKeywordLessThanDiscountRate(
            double lastDiscountRate, String keyword, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.discountRate = :lastDiscountRate AND o.id < :lastId)
               AND (o.meetingAddress LIKE :keyword%)
               AND (o.offeringStatus IN ('AVAILABLE', 'FULL', 'IMMINENT'))
            ORDER BY o.discountRate DESC, o.id DESC
            """)
    List<OfferingEntity> findHighDiscountOfferingsWithMeetingAddressKeywordEqualDiscountRate(
            double lastDiscountRate, Long lastId, String keyword, Pageable pageable);

    // ============================================================
//    @Query("""
//            SELECT o
//            FROM OfferingEntity o
//            WHERE (o.offeringStatus IN ('AVAILABLE', 'IMMINENT'))
//               AND (o.id < :lastId)
//               AND (:keyword IS NULL OR o.title LIKE :keyword% OR o.meetingAddress LIKE :keyword%)
//            ORDER BY o.id DESC
//            """)
//    List<OfferingEntity> findJoinableOfferingsWithKeyword(Long lastId, String keyword, Pageable pageable);

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
               AND (o.title LIKE :keyword%)
               AND (o.offeringStatus IN ('AVAILABLE', 'IMMINENT'))
            ORDER BY o.id DESC
            """)
    List<OfferingEntity> findJoinableOfferingsWithTitleKeyword(Long lastId, String keyword, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.id < :lastId)
               AND (o.meetingAddress LIKE :keyword%)
               AND (o.offeringStatus IN ('AVAILABLE', 'IMMINENT'))
            ORDER BY o.id DESC
            """)
    List<OfferingEntity> findJoinableOfferingsWithMeetingAddressKeyword(Long lastId, String keyword, Pageable pageable);
    // ============================================================

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
