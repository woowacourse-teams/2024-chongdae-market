package com.zzang.chongdae.member.service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@Component
public class NicknameWordInitializer {

    private static final String ADJECTIVE_FILE_PATH = "src/test/resources/static/nickname/adjectives.txt";
    private static final String NOUNS_FILE_PATH = "src/test/resources/static/nickname/nouns.txt";

    private final NicknameWordPicker nicknameWordPicker;
    private final NicknameWordReader nickNameWordReader;
    private List<String> adjectives;
    private List<String> nouns;

    @PostConstruct
    public void init() {
        adjectives = nickNameWordReader.read(ADJECTIVE_FILE_PATH);
        nouns = nickNameWordReader.read(NOUNS_FILE_PATH);
    }

    public String pickAdjective() {
        return nicknameWordPicker.pick(adjectives);
    }

    public String pickNoun() {
        return nicknameWordPicker.pick(nouns);
    }
}
