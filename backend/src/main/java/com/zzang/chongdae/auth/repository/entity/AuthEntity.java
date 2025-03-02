package com.zzang.chongdae.auth.repository.entity;

import com.zzang.chongdae.global.repository.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(name = "auth")
@Entity
public class AuthEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long memberId;

    private String deviceId;

    @NotNull
    private String refreshToken;

    public AuthEntity(Long memberId, String deviceId, String refreshToken) {
        this(null, memberId, deviceId, refreshToken);
    }

    public boolean isValid(String refreshToken) {
        return this.refreshToken.equals(refreshToken);
    }

    public void refresh(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
