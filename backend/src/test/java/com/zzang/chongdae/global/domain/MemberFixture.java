package com.zzang.chongdae.global.domain;

import com.zzang.chongdae.member.domain.AuthProvider;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberFixture {

    @Autowired
    private MemberRepository memberRepository;

    public MemberEntity createMember() {
        return createMember("dora");
    }

    public MemberEntity createMember(String nickname) {
        MemberEntity member = new MemberEntity(
                nickname,
                AuthProvider.KAKAO,
                AuthProvider.KAKAO.buildLoginId(nickname),
                "1234");
        return memberRepository.save(member);
    }
}
