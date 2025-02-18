package com.zzang.chongdae.member.service;

import com.zzang.chongdae.auth.config.TestAuthClientConfig;
import com.zzang.chongdae.member.config.TestNicknameWordPickerConfig;
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
public class NicknameGeneratorTest {

    @Autowired
    NicknameGenerator nickNameGenerator;

    @DisplayName("닉네임 생성에 성공한다.")
    @Test
    void should_returnNickname_when_generateNickName() {
        // given
        String expected = "춤추는장성";

        // when
        String actual = nickNameGenerator.generate();

        // then
        Assertions.assertEquals(actual, expected);
    }
}
