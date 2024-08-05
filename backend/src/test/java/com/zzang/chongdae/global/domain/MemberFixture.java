package com.zzang.chongdae.global.domain;

import com.zzang.chongdae.auth.service.PasswordEncoder;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberFixture {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public MemberEntity createMember() {
        MemberEntity member = new MemberEntity("dora", passwordEncoder.encode("dora1234"));
        return memberRepository.save(member);
    }

    public MemberEntity createMember(String nickname) {
        MemberEntity member = new MemberEntity(nickname, passwordEncoder.encode("5678" + nickname));
        return memberRepository.save(member);
    }
}
