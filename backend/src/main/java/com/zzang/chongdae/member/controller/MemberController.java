package com.zzang.chongdae.member.controller;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.member.service.MemberService;
import com.zzang.chongdae.member.service.dto.NicknameRequest;
import com.zzang.chongdae.member.service.dto.NicknameResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/member")
    ResponseEntity<NicknameResponse> changeNickName(
            @RequestBody @Valid NicknameRequest request,
            MemberEntity member) {
        NicknameResponse nicknameResponse = memberService.changeNickname(member, request);
        return ResponseEntity.ok(nicknameResponse);
    }
}
