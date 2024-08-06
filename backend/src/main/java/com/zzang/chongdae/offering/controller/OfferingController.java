package com.zzang.chongdae.offering.controller;

import com.zzang.chongdae.comment.service.dto.CommentRoomStatusResponse;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.service.OfferingService;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponse;
import com.zzang.chongdae.offering.service.dto.OfferingDetailResponse;
import com.zzang.chongdae.offering.service.dto.OfferingFilterAllResponse;
import com.zzang.chongdae.offering.service.dto.OfferingMeetingResponse;
import com.zzang.chongdae.offering.service.dto.OfferingProductImageRequest;
import com.zzang.chongdae.offering.service.dto.OfferingProductImageResponse;
import com.zzang.chongdae.offering.service.dto.OfferingSaveRequest;
import com.zzang.chongdae.offering.service.dto.OfferingStatusResponse;
import jakarta.validation.Valid;
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

    private final OfferingService offeringService;

    @GetMapping("/offerings/{offering-id}")
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
            @RequestParam(value = "page-size", defaultValue = "10") Integer pageSize) {
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

    @PostMapping("/offerings")
    public ResponseEntity<Void> saveOffering(
            @RequestBody @Valid OfferingSaveRequest request,
            MemberEntity member) {
        Long offeringId = offeringService.saveOffering(request, member);
        return ResponseEntity.created(URI.create("/offerings/" + offeringId)).build();
    }

    @GetMapping("/offerings/{offering-id}/status")
    public ResponseEntity<OfferingStatusResponse> getOfferingStatus(
            @PathVariable(value = "offering-id") Long offeringId) { // TODO : 로그인 사용자가 참여자인지 확인
        OfferingStatusResponse response = offeringService.getOfferingStatus(offeringId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/offerings/{offering-id}/status")
    public ResponseEntity<CommentRoomStatusResponse> updateCommentRoomStatus(
            @PathVariable(value = "offering-id") Long offeringId,
            MemberEntity member) {
        CommentRoomStatusResponse response = offeringService.updateCommentRoomStatus(offeringId, member);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/offerings/product-images/s3")
    public ResponseEntity<OfferingProductImageResponse> uploadProductImage(
            @RequestParam MultipartFile image) {
        OfferingProductImageResponse response = offeringService.uploadProductImage(image);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/offerings/product-images/og")
    public ResponseEntity<OfferingProductImageResponse> extractProductImage(
            @RequestBody @Valid OfferingProductImageRequest request) {
        OfferingProductImageResponse response = offeringService.extractProductImage(request);
        return ResponseEntity.ok(response);
    }
}
