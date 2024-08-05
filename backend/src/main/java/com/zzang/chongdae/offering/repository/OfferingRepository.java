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
            WHERE o.id < :lastId AND (o.title LIKE %:keyword% OR o.meetingAddress LIKE %:keyword%)
            ORDER BY o.id DESC
            """)
    List<OfferingEntity> findRecentOfferingsWithKeyword(Long lastId, String keyword, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE (o.deadline > :lastDeadline OR (o.deadline = :lastDeadline AND o.id < :lastId))
                AND (o.title LIKE %:keyword% OR o.meetingAddress LIKE %:keyword%)
            ORDER BY o.deadline ASC, o.id DESC
            """)
    List<OfferingEntity> findImminentOfferingsWithKeyword(
            LocalDateTime lastDeadline, Long lastId, String keyword, Pageable pageable);

    @Query("""
            SELECT o
            FROM OfferingEntity o
            WHERE ((o.eachPrice - (o.totalPrice * 1.0 / o.totalCount)) / o.eachPrice < :discountRate
                    OR ((o.eachPrice - (o.totalPrice * 1.0 / o.totalCount)) / o.eachPrice = :discountRate AND o.id < :lastId))
                AND (o.title LIKE %:keyword% OR o.meetingAddress LIKE %:keyword%)
            ORDER BY (o.eachPrice - (o.totalPrice * 1.0 / o.totalCount)) / o.eachPrice DESC, o.id DESC
            """)
    List<OfferingEntity> findHighDiscountOfferingsWithKeyword(
            double discountRate, Long lastId, String keyword, Pageable pageable);

    @Query("SELECT MAX(o.id) FROM OfferingEntity o")
    Long findMaxId();
}
