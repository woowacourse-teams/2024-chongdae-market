package com.zzang.chongdae.global.domain;

import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberFixture {

    @Autowired
    private MemberRepository memberRepository;

    public MemberEntity createMember() {
        MemberEntity member = new MemberEntity("dora");
        return memberRepository.save(member);
    }
}
