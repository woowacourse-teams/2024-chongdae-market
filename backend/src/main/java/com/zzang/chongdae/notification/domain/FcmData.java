package com.zzang.chongdae.notification.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FcmData {

    private final Map<String, String> data = new HashMap<>();

    public void addData(String key, String value) {
        this.data.put(key, value);
    }

    public Map<String, String> getData() {
        return Collections.unmodifiableMap(data);
    }
}
