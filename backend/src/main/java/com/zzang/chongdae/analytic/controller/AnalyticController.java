package com.zzang.chongdae.analytic.controller;

import com.zzang.chongdae.analytic.service.AnalyticService;
import com.zzang.chongdae.analytic.service.dto.VariantResponse;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AnalyticController {

    private final AnalyticService analyticService;

    @GetMapping("/analytics/variant")
    public ResponseEntity<VariantResponse> getVariant(MemberEntity member) {
        VariantResponse response = analyticService.getAssignedVariant(member);
        return ResponseEntity.ok(response);
    }
}
