package com.zzang.chongdae.offeringmember.controller;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offeringmember.service.OfferingMemberService;
import com.zzang.chongdae.offeringmember.service.dto.ParticipantResponse;
import com.zzang.chongdae.offeringmember.service.dto.ParticipationRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @DeleteMapping("/participations/{offering-id}")
    public ResponseEntity<Void> cancelParticipate(
            @PathVariable(value = "offering-id") Long offeringId,
            MemberEntity member) {
        offeringMemberService.cancelParticipate(offeringId, member);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/participants")
    public ResponseEntity<ParticipantResponse> getAllParticipant(
            @RequestParam(value = "offering-id") Long offeringId,
            MemberEntity member) {
        ParticipantResponse response = offeringMemberService.getAllParticipant(offeringId, member);
        return ResponseEntity.ok(response);
    }
}
