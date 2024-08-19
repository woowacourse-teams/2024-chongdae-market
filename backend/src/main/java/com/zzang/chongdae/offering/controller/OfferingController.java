package com.zzang.chongdae.offering.controller;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.service.OfferingService;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponse;
import com.zzang.chongdae.offering.service.dto.OfferingDetailResponse;
import com.zzang.chongdae.offering.service.dto.OfferingFilterAllResponse;
import com.zzang.chongdae.offering.service.dto.OfferingMeetingResponse;
import com.zzang.chongdae.offering.service.dto.OfferingMeetingUpdateRequest;
import com.zzang.chongdae.offering.service.dto.OfferingProductImageRequest;
import com.zzang.chongdae.offering.service.dto.OfferingProductImageResponse;
import com.zzang.chongdae.offering.service.dto.OfferingSaveRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class OfferingController {

    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 60;

    private final OfferingService offeringService;

    @GetMapping("/offerings/{offering-id}/detail")
    public ResponseEntity<OfferingDetailResponse> getOfferingDetail(
            @PathVariable(value = "offering-id") Long offeringId,
            MemberEntity member) {
        OfferingDetailResponse response = offeringService.getOfferingDetail(offeringId, member);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/offerings")
    public ResponseEntity<OfferingAllResponse> getAllOffering(
            @RequestParam(value = "filter", defaultValue = "RECENT") String filterName,
            @RequestParam(value = "search", required = false) String searchKeyword,
            @RequestParam(value = "last-id", required = false) Long lastId,
            @RequestParam(value = "page-size", defaultValue = "10") @Min(MIN_PAGE_SIZE) @Max(MAX_PAGE_SIZE)
            Integer pageSize) {
        OfferingAllResponse response = offeringService.getAllOffering(filterName, searchKeyword, lastId, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/offerings/filters")
    public ResponseEntity<OfferingFilterAllResponse> getAllOfferingFilter() {
        OfferingFilterAllResponse response = offeringService.getAllOfferingFilter();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/offerings/{offering-id}/meetings")
    public ResponseEntity<OfferingMeetingResponse> getOfferingMeeting(
            @PathVariable(value = "offering-id") Long offeringId,
            MemberEntity member) {
        OfferingMeetingResponse response = offeringService.getOfferingMeeting(offeringId, member);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/offerings/{offering-id}/meetings")
    public ResponseEntity<OfferingMeetingResponse> updateOfferingMeeting(
            @PathVariable(value = "offering-id") Long offeringId,
            @RequestBody @Valid OfferingMeetingUpdateRequest request,
            MemberEntity member) {
        OfferingMeetingResponse response = offeringService.updateOfferingMeeting(offeringId, request, member);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/offerings")
    public ResponseEntity<Void> saveOffering(
            @RequestBody @Valid OfferingSaveRequest request,
            MemberEntity member) {
        Long offeringId = offeringService.saveOffering(request, member);
        return ResponseEntity.created(URI.create("/offerings/" + offeringId)).build();
    }

    @PostMapping("/offerings/product-images/s3")
    public ResponseEntity<OfferingProductImageResponse> uploadProductImageToS3(
            @RequestParam MultipartFile image) {
        OfferingProductImageResponse response = offeringService.uploadProductImageToS3(image);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/offerings/product-images/og")
    public ResponseEntity<OfferingProductImageResponse> extractProductImageFromOg(
            @RequestBody @Valid OfferingProductImageRequest request) {
        OfferingProductImageResponse response = offeringService.extractProductImageFromOg(request);
        return ResponseEntity.ok(response);
    }
}
