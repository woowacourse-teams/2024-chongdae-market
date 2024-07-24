package com.zzang.chongdae.offeringmember.controller;

import com.zzang.chongdae.offeringmember.service.OfferingMemberService;
import com.zzang.chongdae.offeringmember.service.dto.ParticipationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "OfferingMember(공모인원)")
@RequiredArgsConstructor
@Controller
public class OfferingMemberController {

    private final OfferingMemberService offeringMemberService;

    @Operation(summary = "공모 참여", description = "게시된 공모에 참여합니다.")
    @PostMapping("/participations")
    ResponseEntity<Void> participate(@RequestBody ParticipationRequest participationRequest) {
        Long id = offeringMemberService.participate(participationRequest);
        return ResponseEntity.created(URI.create("participations/" + id)).build();
    }
}
