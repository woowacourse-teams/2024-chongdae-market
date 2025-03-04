package com.zzang.chongdae.auth.repository;

import com.zzang.chongdae.auth.repository.entity.RefreshTokenEntity;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByMemberAndDeviceId(MemberEntity member, String deviceId);

    boolean existsByMemberAndDeviceId(MemberEntity member, String deviceId);
}
