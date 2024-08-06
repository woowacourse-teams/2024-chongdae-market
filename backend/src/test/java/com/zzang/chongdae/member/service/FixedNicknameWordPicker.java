package com.zzang.chongdae.member.service;


import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FixedNicknameWordPicker implements NicknameWordPicker {

    @Override
    public String pick(List<String> words) {
        return words.get(0);
    }
}
