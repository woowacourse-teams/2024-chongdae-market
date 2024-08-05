package com.zzang.chongdae.member.service;

import com.zzang.chongdae.member.config.NicknameWordPickerConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = {NicknameWordPickerConfiguration.class})
public class NicknameGeneratorTest {

    @Autowired
    NickNameGenerator nickNameGenerator;

    @DisplayName("닉네임 생성에 성공한다.")
    @Test
    void should_returnNickname_when_generateNickName() {
        // given
        String expected = "춤추는해";

        // when
        String actual = nickNameGenerator.generate();

        // then
        Assertions.assertEquals(actual, expected);
    }
}
