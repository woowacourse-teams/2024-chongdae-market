package com.zzang.chongdae.offering.controller;

import com.zzang.chongdae.offering.service.OfferingService;
import com.zzang.chongdae.offering.service.dto.OfferingDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class OfferingController {

    private final OfferingService offeringService;

    @GetMapping("/offerings/{offering-id}")
    public ResponseEntity<OfferingDetailResponse> getOfferingDetail(@PathVariable(value = "offering-id") Long id) {
        OfferingDetailResponse response = offeringService.getOfferingDetail(id);
        return ResponseEntity.ok(response);
    }
}
