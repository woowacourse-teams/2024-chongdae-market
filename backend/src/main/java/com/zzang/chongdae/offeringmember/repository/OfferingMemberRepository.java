package com.zzang.chongdae.offeringmember.repository;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferingMemberRepository extends JpaRepository<OfferingMemberEntity, Long> {

    int countByOffering(OfferingEntity offering);
}
