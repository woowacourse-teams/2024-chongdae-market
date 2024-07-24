package com.zzang.chongdae.offering.domain;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.domain.OfferingMemberRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OfferingWithRole {

    private final OfferingEntity offering;
    private final OfferingMemberRole role;
}
