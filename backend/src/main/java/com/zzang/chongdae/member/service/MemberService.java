package com.zzang.chongdae.member.service;

import com.zzang.chongdae.global.config.WriterDatabase;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.exception.MemberErrorCode;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.member.service.dto.NicknameRequest;
import com.zzang.chongdae.member.service.dto.NicknameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @WriterDatabase
    @Transactional
    public NicknameResponse changeNickname(MemberEntity member, NicknameRequest request) {
        String nickname = request.nickname();
        MemberEntity targetMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new MarketException(MemberErrorCode.NOT_FOUND));
        if (memberRepository.existsByNickname(nickname)) {
            throw new MarketException(MemberErrorCode.NICK_NAME_ALREADY_EXIST);
        }
        targetMember.updateNickName(nickname);
        return new NicknameResponse(nickname);
    }
}
