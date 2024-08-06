package com.zzang.chongdae.auth.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class SHA256PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String rawPassword) {
        return hashPassword(rawPassword);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }
}
