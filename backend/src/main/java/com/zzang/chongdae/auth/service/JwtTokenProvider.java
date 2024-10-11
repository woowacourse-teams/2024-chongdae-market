package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.auth.exception.AuthErrorCode;
import com.zzang.chongdae.auth.service.dto.AuthTokenDto;
import com.zzang.chongdae.global.exception.MarketException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Clock;
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
    private final Clock clock;

    public JwtTokenProvider(@Value("${security.jwt.token.access-secret-key}") String accessSecretKey,
                            @Value("${security.jwt.token.refresh-secret-key}") String refreshSecretKey,
                            @Value("${security.jwt.token.access-token-expired}") Duration accessTokenExpired,
                            @Value("${security.jwt.token.refresh-token-expired}") Duration refreshTokenExpired,
                            Clock clock) {
        this.accessSecretKey = Base64.getEncoder().encodeToString(accessSecretKey.getBytes());
        this.refreshSecretKey = Base64.getEncoder().encodeToString(refreshSecretKey.getBytes());
        this.accessTokenExpired = accessTokenExpired;
        this.refreshTokenExpired = refreshTokenExpired;
        this.clock = clock;
    }

    public AuthTokenDto createAuthToken(String payload) {
        return new AuthTokenDto(createToken(payload, accessSecretKey, accessTokenExpired),
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
        Date now = Date.from(clock.instant());
        return new Date(now.getTime() + expired.toMillis());
    }

    public void validateAccessToken(String token) {
        getClaimsAccessToken(token, accessSecretKey).getSubject();
    }

    public Long getMemberIdByAccessToken(String token) {
        String memberId = getClaimsAccessToken(token, accessSecretKey).getSubject();
        return Long.valueOf(memberId);
    }

    private Claims getClaimsAccessToken(String token, String accessSecretKey) {
        try {
            return getClaims(token, accessSecretKey);
        } catch (ExpiredJwtException e) {
            throw new MarketException(AuthErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new MarketException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    private Claims getClaims(String token, String key) {
        return Jwts.parser()
                .setSigningKey(key)
                .setClock(() -> Date.from(clock.instant()))
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getMemberIdByRefreshToken(String token) {
        String memberId = getClaimsRefreshToken(token, refreshSecretKey).getSubject();
        return Long.valueOf(memberId);
    }

    private Claims getClaimsRefreshToken(String token, String refreshSecretKey) {
        try {
            return getClaims(token, refreshSecretKey);
        } catch (ExpiredJwtException e) {
            throw new MarketException(AuthErrorCode.EXPIRED_REFRESH_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new MarketException(AuthErrorCode.INVALID_TOKEN);
        }
    }
}
