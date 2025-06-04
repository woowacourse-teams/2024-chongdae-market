package com.zzang.chongdae.offeringmember.controller;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offeringmember.service.OfferingMemberService;
import com.zzang.chongdae.offeringmember.service.dto.ChangeSettleRequest;
import com.zzang.chongdae.offeringmember.service.dto.ChangeSettleResponse;
import com.zzang.chongdae.offeringmember.service.dto.ParticipantResponse;
import com.zzang.chongdae.offeringmember.service.dto.ParticipationRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OfferingMemberController {

    private final OfferingMemberService offeringMemberService;

    @PostMapping("/participations")
    public ResponseEntity<Void> participate(
            @RequestBody @Valid ParticipationRequest request,
            MemberEntity member) {
        Long id = offeringMemberService.participate(request, member);
        return ResponseEntity.created(URI.create("/participations/" + id)).build();
    }

    @DeleteMapping("/participations")
    public ResponseEntity<Void> cancelParticipate(
            @RequestParam(value = "offering-id") Long offeringId,
            MemberEntity member) {
        offeringMemberService.cancelParticipate(offeringId, member);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/participants")
    public ResponseEntity<ParticipantResponse> getAllParticipant(
            @RequestParam(value = "offering-id") Long offeringId,
            MemberEntity member) {
        ParticipantResponse response = offeringMemberService.getAllParticipant(offeringId, member);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/participants")
    public ResponseEntity<ChangeSettleResponse> changeSettleStatus(
            @RequestBody ChangeSettleRequest request,
            MemberEntity member) {
        ChangeSettleResponse response = offeringMemberService.changeSettleStatus(request, member);
        return ResponseEntity.ok(response);
    }
}
