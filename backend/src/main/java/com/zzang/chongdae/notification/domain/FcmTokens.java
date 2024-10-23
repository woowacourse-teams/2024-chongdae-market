package com.zzang.chongdae.notification.domain;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import java.util.List;

public class FcmTokens {

    private final List<FcmToken> tokens;

    private FcmTokens(List<FcmToken> tokens) {
        this.tokens = tokens;
    }

    public static FcmTokens from(List<MemberEntity> members) {
        List<FcmToken> tokens = members.stream()
                .map(FcmToken::new)
                .toList();
        return new FcmTokens(tokens);
    }

    public List<String> getTokenValues() {
        return tokens.stream()
                .map(FcmToken::getValue)
                .toList();
    }

    public boolean isEmpty() {
        return tokens.isEmpty();
    }
}
