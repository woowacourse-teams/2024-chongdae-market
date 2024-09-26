package com.zzang.chongdae.offeringmember.repository;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OfferingMemberRepository extends JpaRepository<OfferingMemberEntity, Long> {

    Boolean existsByOfferingAndMember(OfferingEntity offering, MemberEntity member);

    List<OfferingMemberEntity> findAllByOffering(OfferingEntity offering);

    Optional<OfferingMemberEntity> findByOfferingAndMember(OfferingEntity offering, MemberEntity member);

    Optional<OfferingMemberEntity> findByOfferingIdAndMember(Long offeringId, MemberEntity member);


    @Query(value = """
            SELECT om.offering_id
            FROM offering_member as om
            WHERE om.member_id = :member_id
            """, nativeQuery = true)
    List<Long> findOfferingIdsByMember(@Param("member_id") Long memberId);
}
