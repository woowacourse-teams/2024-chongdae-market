package com.zzang.chongdae.member.service;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.exception.MemberErrorCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class NicknameWordReader {

    public List<String> read(String path) {
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
             BufferedReader br = new BufferedReader(inputStreamReader)) {
            String[] line = br.readLine().split(",");
            return Arrays.asList(line);
        } catch (IOException e) {
            throw new MarketException(MemberErrorCode.NICK_NAME_READ_FAIL);
        }
    }
}
