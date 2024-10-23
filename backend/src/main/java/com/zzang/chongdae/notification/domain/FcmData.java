package com.zzang.chongdae.notification.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FcmData {

    private final Map<String, String> data = new HashMap<>();

    public void addData(String key, Object value) {
        data.put(key, value.toString());
    }

    public Map<String, String> getData() {
        data.forEach(this::logWithoutBody);
        return Collections.unmodifiableMap(data);
    }

    private void logWithoutBody(String key, String value) {
        if (!key.equals("body")) {
            log.info("{} : {}", key, value);
        }
    }
}
