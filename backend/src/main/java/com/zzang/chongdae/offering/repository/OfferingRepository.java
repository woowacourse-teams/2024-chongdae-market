package com.zzang.chongdae.offering.repository;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.OfferingWithRole;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OfferingRepository extends JpaRepository<OfferingEntity, Long> {

    List<OfferingEntity> findByIdGreaterThan(Long lastId, Pageable pageable);

    @Query("""
            SELECT new com.zzang.chongdae.offering.domain.OfferingWithRole(
                o, om.role
            )
            FROM OfferingEntity as o JOIN OfferingMemberEntity as om
                ON o.id = om.offering.id
            WHERE om.member = :member
        """)
    List<OfferingWithRole> findAllByMember(MemberEntity member);
}
