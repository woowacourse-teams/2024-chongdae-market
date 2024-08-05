package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.auth.service.dto.TokenDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final String accessSecretKey;
    private final String refreshSecretKey;
    private final Duration accessTokenExpired;
    private final Duration refreshTokenExpired;

    public JwtTokenProvider(@Value("${security.jwt.token.access-secret-key}") String accessSecretKey,
                            @Value("${security.jwt.token.refresh-secret-key}") String refreshSecretKey,
                            @Value("${security.jwt.token.access-token-expired}") Duration accessTokenExpired,
                            @Value("${security.jwt.token.refresh-token-expired}") Duration refreshTokenExpired) {
        this.accessSecretKey = Base64.getEncoder().encodeToString(accessSecretKey.getBytes());
        this.refreshSecretKey = Base64.getEncoder().encodeToString(refreshSecretKey.getBytes());
        this.accessTokenExpired = accessTokenExpired;
        this.refreshTokenExpired = refreshTokenExpired;
    }

    public TokenDto createAuthToken(String payload) {
        return new TokenDto(createToken(payload, accessSecretKey, accessTokenExpired),
                createToken(payload, refreshSecretKey, refreshTokenExpired));
    }

    private String createToken(String payload, String secretKey, Duration expired) {
        return Jwts.builder()
                .setSubject(payload)
                .setExpiration(calculateExpiredAt(expired))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private Date calculateExpiredAt(Duration expired) {
        Date now = new Date(); // Date.from(clock.instant());
        return new Date(now.getTime() + expired.toMillis());
    }
}
