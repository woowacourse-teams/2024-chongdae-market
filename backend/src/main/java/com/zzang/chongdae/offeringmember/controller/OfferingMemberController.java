package com.zzang.chongdae.offeringmember.controller;

import com.zzang.chongdae.offeringmember.service.OfferingMemberService;
import com.zzang.chongdae.offeringmember.service.dto.ParticipationRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OfferingMemberController {

    private final OfferingMemberService offeringMemberService;

    @PostMapping("/participations")
    ResponseEntity<Void> participate(
            @RequestBody @Valid ParticipationRequest request) {
        Long id = offeringMemberService.participate(request);
        return ResponseEntity.created(URI.create("/participations/" + id)).build();
    }
}
