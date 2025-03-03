package com.zzang.chongdae.auth.repository;

import com.zzang.chongdae.auth.repository.entity.RefreshTokenEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByMemberIdAndDeviceId(Long memberId, String deviceId);

    boolean existsByMemberIdAndDeviceId(Long memberId, String deviceId);
}
