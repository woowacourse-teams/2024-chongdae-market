package com.zzang.chongdae.member.service;

import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class RandomNicknameWordPicker implements NicknameWordPicker {

    private final Random random = new Random();

    @Override
    public String pick(List<String> words) {
        return words.get(random.nextInt(words.size()));
    }
}
