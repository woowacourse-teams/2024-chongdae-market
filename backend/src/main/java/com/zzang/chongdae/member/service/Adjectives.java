package com.zzang.chongdae.member.service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Adjectives {

    private static final String ADJECTIVE_FILE_PATH = "src/test/resources/static/nickname/adjcetives.txt";

    private final NickNameWordReader nickNameWordReader;
    private final NicknameWordPicker nicknameWordPicker;
    private List<String> adjectives = new ArrayList<>();

    @PostConstruct
    public void init() {
        adjectives = nickNameWordReader.read(ADJECTIVE_FILE_PATH);
    }

    public String pick() {
        return nicknameWordPicker.pick(adjectives);
    }
}
