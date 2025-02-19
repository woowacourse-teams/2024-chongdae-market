package com.zzang.chongdae.member.service;

import com.zzang.chongdae.auth.config.TestAuthClientConfig;
import com.zzang.chongdae.global.domain.MemberFixture;
import com.zzang.chongdae.member.config.TestNicknameWordPickerConfig;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.member.service.dto.NicknameRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = {TestNicknameWordPickerConfig.class,
        TestAuthClientConfig.class})
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberFixture memberFixture;

    @DisplayName("닉네임 변경에 성공한다.")
    @Test
    void should_changeNickname_when_givenNickname() {
        // given
        MemberEntity member = memberFixture.createMember("pokidoki");
        NicknameRequest request = new NicknameRequest("dokipoki");

        // when
        memberService.changeNickname(member, request);

        //then
        Assertions.assertTrue(memberRepository.existsByNickname("dokipoki"));
    }
}
