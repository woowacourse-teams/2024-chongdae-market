package com.zzang.chongdae.notification.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FcmData {

    private final Map<String, String> data = new HashMap<>();

    public void addData(String key, Object value) {
        data.put(key, value.toString());
    }

    public Map<String, String> getData() {
        return Collections.unmodifiableMap(data);
    }
}
