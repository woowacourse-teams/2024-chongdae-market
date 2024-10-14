package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.auth.domain.AuthToken;
import com.zzang.chongdae.auth.domain.KakaoMember;
import com.zzang.chongdae.auth.domain.LoginMember;
import com.zzang.chongdae.auth.domain.SignupMember;
import com.zzang.chongdae.auth.service.dto.AuthInfoDto;
import com.zzang.chongdae.auth.service.dto.KakaoLoginRequest;
import com.zzang.chongdae.global.config.WriterDatabase;
import com.zzang.chongdae.member.repository.MemberPersistenceAdaptor;
import com.zzang.chongdae.member.service.NicknameGenerator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberPersistenceAdaptor memberStorage; // TODO: 인터페이스화해서 persistence 영향 없게 변경 필요
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final NicknameGenerator nickNameGenerator;
    private final AuthClient authClient;

    @WriterDatabase
    public AuthInfoDto kakaoLogin(KakaoLoginRequest request) { // TODO: LoginResponseDTO로 이름 변경하기
        KakaoMember kakaoMember = authClient.getKakaoMember(request.accessToken());
        LoginMember loginMember = memberStorage.findByLoginId(kakaoMember.getLoginId())
                .orElseGet(() -> signup(kakaoMember));
        return login(loginMember);
    }

    private LoginMember signup(KakaoMember kakaoMember) {
        String nickname = nickNameGenerator.generate();
        String password = passwordEncoder.encode(UUID.randomUUID().toString());
        SignupMember signupMember = new SignupMember(nickname, password, kakaoMember);
        return memberStorage.save(signupMember);
    }

    private AuthInfoDto login(LoginMember loginMember) {
        AuthToken authToken = jwtTokenProvider.createAuthToken(loginMember.getId().toString());
        return new AuthInfoDto(loginMember, authToken);
    }

    public AuthToken refresh(String refreshToken) {
        Long memberId = jwtTokenProvider.getMemberIdByRefreshToken(refreshToken);
        return jwtTokenProvider.createAuthToken(memberId.toString());
    }
}
