package com.zzang.chongdae.offeringmember.repository;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OfferingMemberRepository extends JpaRepository<OfferingMemberEntity, Long> {

    Boolean existsByOfferingAndMember(OfferingEntity offering, MemberEntity member);

    @Query("""
            SELECT om
            FROM OfferingMemberEntity om
                JOIN FETCH om.member
            WHERE om.offering = :offering
            """)
    List<OfferingMemberEntity> findAllWithMemberByOffering(OfferingEntity offering);

    Optional<OfferingMemberEntity> findByOfferingAndMember(OfferingEntity offering, MemberEntity member);

    Optional<OfferingMemberEntity> findByOfferingIdAndMember(Long offeringId, MemberEntity member);

    @Query("""
            SELECT om.offering.id
            FROM OfferingMemberEntity om
            WHERE om.member.id = :memberId
            """)
    List<Long> findOfferingIdsByMemberId(Long memberId);
}
