package com.zzang.chongdae.auth.service;

public interface PasswordEncoder {

    String encode(String rawPassword);
}
