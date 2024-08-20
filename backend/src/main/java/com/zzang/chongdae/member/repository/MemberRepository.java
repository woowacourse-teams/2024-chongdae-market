package com.zzang.chongdae.member.repository;

import com.zzang.chongdae.member.domain.AuthProvider;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByPassword(String password);

    boolean existsByPassword(String password);

    boolean existsByNickname(String nickname);

    Optional<MemberEntity> findByProviderAndProviderId(AuthProvider provider, String providerId);
}
