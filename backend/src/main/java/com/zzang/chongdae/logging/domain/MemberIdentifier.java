package com.zzang.chongdae.logging.domain;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Base64;

public class MemberIdentifier {

    public static String ACCESS_TOKEN_NAME = "access_token";
    public static String ID_NOT_FOUND_INFO = "Not Found";

    private final String idInfo;

    public MemberIdentifier(Cookie[] cookies) {
        this.idInfo = buildIdInfo(cookies);
    }

    private String buildIdInfo(Cookie[] cookies) {
        if (cookies == null) {
            return ID_NOT_FOUND_INFO;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> ACCESS_TOKEN_NAME.equals(cookie.getName()))
                .findFirst()
                .map(this::getAccessTokenInfo)
                .orElseGet(() -> ID_NOT_FOUND_INFO);
    }

    private String getAccessTokenInfo(Cookie cookie) {
        String jwtToken = cookie.getValue();
        String[] tokenParts = jwtToken.split("\\.");
        if (tokenParts.length == 3) {
            String payload = new String(Base64.getUrlDecoder().decode(tokenParts[1]));
            return payload;
        }
        return ID_NOT_FOUND_INFO;
    }

    public String getIdInfo() {
        return idInfo;
    }
}
