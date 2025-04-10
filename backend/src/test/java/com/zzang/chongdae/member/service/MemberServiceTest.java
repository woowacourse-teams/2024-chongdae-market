package com.zzang.chongdae.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.zzang.chongdae.global.domain.MemberFixture;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.member.service.dto.NicknameRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class MemberServiceTest extends ServiceTest {

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
        NicknameRequest request = new NicknameRequest("test");

        // when
        memberService.changeNickname(member, request);

        //then
        Assertions.assertTrue(memberRepository.existsByNickname("test"));
    }

    @DisplayName("이미 존재하는 닉네임이면 변경이 되지 않는다.")
    @Test
    void should_notChangeNickname_when_changeNicknameAlreadyExist() {
        // given
        MemberEntity member = memberFixture.createMember("pokidoki");
        MemberEntity otherMember = memberFixture.createMember("dokipoki");

        // when
        NicknameRequest request = new NicknameRequest("dokipoki");
        assertThatThrownBy(() -> memberService.changeNickname(member, request))
                .isInstanceOf(MarketException.class);
        MemberEntity actual = memberRepository.findById(member.getId()).get();

        //then
        assertThat(actual.getNickname()).isEqualTo(member.getNickname());
    }
}
