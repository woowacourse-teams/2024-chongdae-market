package com.zzang.chongdae.global.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class DomainSupplier {

    @Autowired
    protected MemberFixture memberFixture;

    @Autowired
    protected OfferingFixture offeringFixture;
}
