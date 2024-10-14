package com.zzang.chongdae.member.service;

import com.zzang.chongdae.auth.domain.LoginMember;
import com.zzang.chongdae.auth.domain.SignupMember;
import java.util.Optional;

public interface MemberStorage {

    Optional<LoginMember> findByLoginId(String loginId);

    LoginMember save(SignupMember signupMember);
}
