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
                AND (:keyword IS NULL OR o.title LIKE %:keyword% OR o.meetingAddress LIKE %:keyword%)
            ORDER BY o.id DESC
            """)
    List<OfferingEntity> findRecentOfferingsWithKeyword(Long lastId, String keyword, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE ((o.meetingDate > :lastMeetingDate AND o.meetingDate < :threshold)
                OR (o.meetingDate = :lastMeetingDate AND o.id < :lastId AND o.meetingDate < :threshold)
                OR (o.totalCount <= 3 AND (o.totalCount - o.currentCount) < 2 AND (o.totalCount - o.currentCount) > 0)
                OR (o.totalCount > 3 AND (o.totalCount - o.currentCount) < 3 AND (o.totalCount - o.currentCount) > 0))
            AND (:keyword IS NULL OR o.title LIKE %:keyword% OR o.meetingAddress LIKE %:keyword%)
            AND (o.isManualConfirmed IS FALSE)
            AND (o.meetingDate >= :now)
            AND ((o.meetingDate > :lastMeetingDate) OR (o.meetingDate = :lastMeetingDate AND o.id < :lastId))
            ORDER BY o.meetingDate ASC, o.id DESC
            """)
    List<OfferingEntity> findImminentOfferingsWithKeyword(
            LocalDateTime now, LocalDateTime threshold, LocalDateTime lastMeetingDate, Long lastId, String keyword,
            Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE ((o.originPrice - (o.totalPrice * 1.0 / o.totalCount)) / o.originPrice < :discountRate
                    OR ((o.originPrice - (o.totalPrice * 1.0 / o.totalCount)) / o.originPrice = :discountRate AND o.id < :lastId))
               AND (:keyword IS NULL OR o.title LIKE %:keyword% OR o.meetingAddress LIKE %:keyword%)
            ORDER BY (o.originPrice - (o.totalPrice * 1.0 / o.totalCount)) / o.originPrice DESC, o.id DESC
            """)
    List<OfferingEntity> findHighDiscountOfferingsWithKeyword(
            Double discountRate, Long lastId, String keyword, Pageable pageable);

    @Query("SELECT MAX(o.id) FROM OfferingEntity o")
    Long findMaxId();
}
