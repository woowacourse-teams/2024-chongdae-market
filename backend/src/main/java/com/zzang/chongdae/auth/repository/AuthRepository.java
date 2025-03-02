package com.zzang.chongdae.auth.repository;

import com.zzang.chongdae.auth.repository.entity.AuthEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<AuthEntity, Long> {

    Optional<AuthEntity> findByMemberIdAndDeviceId(Long memberId, String deviceId);
}
