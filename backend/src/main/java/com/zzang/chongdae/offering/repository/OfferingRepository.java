package com.zzang.chongdae.offering.repository;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.OfferingWithRole;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OfferingRepository extends JpaRepository<OfferingEntity, Long> {

    @Query("""
                SELECT new com.zzang.chongdae.offering.domain.OfferingWithRole(o, om.role)
                FROM OfferingEntity as o JOIN OfferingMemberEntity as om
                    ON o.id = om.offering.id
                WHERE om.member = :member
            """)
    List<OfferingWithRole> findAllWithRoleByMember(MemberEntity member);

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
            WHERE ((o.deadline > :now AND o.deadline < :threshold)
                OR (o.deadline = :now AND o.id < :lastId)
                OR (o.totalCount <= 3 AND (o.totalCount - o.currentCount) < 2)
                OR (o.totalCount > 3 AND (o.totalCount - o.currentCount) < 3))
            AND (:keyword IS NULL OR o.title LIKE %:keyword% OR o.meetingAddress LIKE %:keyword%)
            AND (o.isManualConfirmed IS FALSE)
            ORDER BY o.deadline ASC, o.id DESC
            """)
    List<OfferingEntity> findImminentOfferingsWithKeyword(
            LocalDateTime now, LocalDateTime threshold, Long lastId, String keyword, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE ((o.originPrice - (o.totalPrice * 1.0 / o.totalCount)) / o.originPrice < :discountRate
                    OR ((o.originPrice - (o.totalPrice * 1.0 / o.totalCount)) / o.originPrice = :discountRate AND o.id < :lastId))
               AND (:keyword IS NULL OR o.title LIKE %:keyword% OR o.meetingAddress LIKE %:keyword%)
            ORDER BY (o.originPrice - (o.totalPrice * 1.0 / o.totalCount)) / o.originPrice DESC, o.id DESC
            """)
    List<OfferingEntity> findHighDiscountOfferingsWithKeyword(
            double discountRate, Long lastId, String keyword, Pageable pageable);

    @Query("SELECT MAX(o.id) FROM OfferingEntity o")
    Long findMaxId();
}
