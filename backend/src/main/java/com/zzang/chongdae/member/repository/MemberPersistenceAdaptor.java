package com.zzang.chongdae.member.repository;

import com.zzang.chongdae.auth.domain.LoginMember;
import com.zzang.chongdae.auth.domain.SignupMember;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.member.service.MemberStorage;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberPersistenceAdaptor implements MemberStorage {

    private final MemberRepository memberRepository;

    @Override
    public Optional<LoginMember> findByLoginId(String loginId) {
        Optional<MemberEntity> member = memberRepository.findByLoginId(loginId);
        return member.map(MemberEntity::toLoginMember);
    }

    @Override
    public LoginMember save(SignupMember signupMember) {
        MemberEntity member = new MemberEntity(signupMember);
        MemberEntity savedMember = memberRepository.save(member);
        return savedMember.toLoginMember();
    }
}
