package com.zzang.chongdae.member.repository.entity;

import com.zzang.chongdae.global.repository.entity.BaseTimeEntity;
import com.zzang.chongdae.member.domain.AuthProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "member")
@Entity
public class MemberEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true, length = 10)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @NotNull
    @Column(unique = true)
    private String loginId;

    @NotNull
    private String password; // TODO: 일반 로그인 들어올 시 salt 추가

    @NotNull
    @Column(unique = true)
    private String fcmToken;

    public MemberEntity(String nickname, AuthProvider provider, String loginId, String password, String fcmToken) {
        this(null, nickname, provider, loginId, password, fcmToken);
    }

    public boolean isSame(MemberEntity other) {
        return this.equals(other);
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
