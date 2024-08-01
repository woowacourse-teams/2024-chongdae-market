package com.zzang.chongdae.offering.controller;

import com.zzang.chongdae.offering.service.OfferingService;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponse;
import com.zzang.chongdae.offering.service.dto.OfferingDetailResponse;
import com.zzang.chongdae.offering.service.dto.OfferingMeetingResponse;
import com.zzang.chongdae.offering.service.dto.OfferingProductImageRequest;
import com.zzang.chongdae.offering.service.dto.OfferingProductImageResponse;
import com.zzang.chongdae.offering.service.dto.OfferingSaveRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OfferingController {

    private final OfferingService offeringService;

    @GetMapping("/offerings/{offering-id}")
    public ResponseEntity<OfferingDetailResponse> getOfferingDetail(
            @PathVariable(value = "offering-id") Long offeringId, @RequestParam(value = "member-id") Long memberId) {
        OfferingDetailResponse response = offeringService.getOfferingDetail(offeringId, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/offerings")
    public ResponseEntity<OfferingAllResponse> getAllOffering(
            @RequestParam(value = "last-id", defaultValue = "0") Long lastId,
            @RequestParam(value = "page-size", defaultValue = "10") Integer pageSize) {
        OfferingAllResponse response = offeringService.getAllOffering(lastId, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/offerings/{offering-id}/meetings")
    public ResponseEntity<OfferingMeetingResponse> getOfferingMeeting(
            @PathVariable(value = "offering-id") Long offeringId) {
        OfferingMeetingResponse response = offeringService.getOfferingMeeting(offeringId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/offerings")
    public ResponseEntity<Void> saveOffering(
            @RequestBody OfferingSaveRequest request) {
        Long offeringId = offeringService.saveOffering(request);
        return ResponseEntity.created(URI.create("/offerings/" + offeringId)).build();
    }

    @PostMapping("/offerings/product-images")
    public ResponseEntity<OfferingProductImageResponse> extractProductImage(
            @RequestBody OfferingProductImageRequest request) {
        OfferingProductImageResponse response = offeringService.extractProductImage(request);
        return ResponseEntity.ok(response);
    }
}
