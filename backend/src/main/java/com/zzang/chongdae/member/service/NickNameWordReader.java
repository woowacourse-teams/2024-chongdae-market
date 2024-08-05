package com.zzang.chongdae.member.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class NickNameWordReader {

    public List<String> read(String path) {
        try (FileInputStream fileInputStream = new FileInputStream(path);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader br = new BufferedReader(inputStreamReader)) {
            String[] line = br.readLine().split(",");
            return Arrays.asList(line);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
