package com.zzang.chongdae.offeringmember.repository;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferingMemberRepository extends JpaRepository<OfferingMemberEntity, Long> {

    int countByOffering(OfferingEntity offering);

    Boolean existsByOfferingAndMember(OfferingEntity offering, MemberEntity member);

    List<OfferingMemberEntity> findAllByOffering(OfferingEntity offering);

    Optional<OfferingMemberEntity> findByOfferingAndMember(OfferingEntity offering, MemberEntity member);
}
