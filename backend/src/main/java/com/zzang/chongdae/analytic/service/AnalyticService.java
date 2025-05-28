package com.zzang.chongdae.analytic.service;

import com.zzang.chongdae.analytic.domain.VariantType;
import com.zzang.chongdae.analytic.service.dto.VariantResponse;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import org.springframework.stereotype.Service;

@Service
public class AnalyticService {

    public VariantResponse getAssignedVariant(MemberEntity member) {
        VariantType type = VariantType.calculateByMember(member);
        return new VariantResponse(type);
    }
}
