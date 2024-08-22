package com.zzang.chongdae.member.service;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.exception.MemberErrorCode;
import com.zzang.chongdae.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NicknameGenerator {

    private static final int MAX_TRY_COUNT = 3;

    private final MemberRepository memberRepository;
    private final NicknameWordInitializer nicknameWordInitializer;

    public String generate() {
        int tryCount = 0;
        String nickname = tryGenerate(tryCount);
        while (memberRepository.existsByNickname(nickname)) {
            nickname = tryGenerate(tryCount++);
        }
        return nickname;
    }

    private String tryGenerate(int tryCount) {
        if (tryCount == MAX_TRY_COUNT) {
            throw new MarketException(MemberErrorCode.MAX_TRY_EXCEEDED);
        }
        String adjective = nicknameWordInitializer.pickAdjective();
        String noun = nicknameWordInitializer.pickNoun();
        return adjective + noun;
    }
}
