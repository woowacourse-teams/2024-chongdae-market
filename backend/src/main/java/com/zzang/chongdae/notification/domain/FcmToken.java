package com.zzang.chongdae.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FcmToken {

    private final String value;
}
