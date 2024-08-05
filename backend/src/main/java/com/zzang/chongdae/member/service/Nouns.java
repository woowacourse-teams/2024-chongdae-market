package com.zzang.chongdae.member.service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@Component
public class Nouns {

    private static final String NOUNS_FILE_PATH = "src/test/resources/static/nickname/nouns.txt";

    private final NickNameWordReader nickNameWordReader;
    private final NicknameWordPicker nicknameWordPicker;
    private List<String> nouns = new ArrayList<>();

    @PostConstruct
    public void init() {
        nouns = nickNameWordReader.read(NOUNS_FILE_PATH);
    }

    public String pick() {
        return nicknameWordPicker.pick(nouns);
    }
}
