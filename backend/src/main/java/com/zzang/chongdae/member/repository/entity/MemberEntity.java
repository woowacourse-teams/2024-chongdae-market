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
import jakarta.persistence.UniqueConstraint;
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
@Table(
        name = "member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"provider", "providerId"})
        }
)
@Entity
public class MemberEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true, length = 10)
    private String nickname;

    @NotNull
    private String password; // TODO: 지우기

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    public MemberEntity(String nickname, String password) {
        this(null, nickname, password, AuthProvider.KAKAO, ""); // TODO: 지우기
    }

    public MemberEntity(String nickname, AuthProvider provider, String providerId) {
        this(null, nickname, "", provider, providerId); // TODO: 지우기
    }

    public boolean isSame(MemberEntity other) {
        return this.equals(other);
    }
}
