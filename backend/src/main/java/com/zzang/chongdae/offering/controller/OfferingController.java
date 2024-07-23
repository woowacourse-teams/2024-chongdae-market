package com.zzang.chongdae.offering.controller;

import com.zzang.chongdae.offering.service.OfferingService;
import com.zzang.chongdae.offering.service.dto.OfferingDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Offerring(공모)")
@RequiredArgsConstructor
@Controller
public class OfferingController {

    private final OfferingService offeringService;

    @Operation(summary = "공모 상세 조회", description = "공모 id를 통해 공모의 상세 정보를 조회합니다.")
    @GetMapping("/offerings/{offering-id}")
    public ResponseEntity<OfferingDetailResponse> getOfferingDetail(@PathVariable(value = "offering-id") Long id) {
        OfferingDetailResponse response = offeringService.getOfferingDetail(id);
        return ResponseEntity.ok(response);
    }
}
