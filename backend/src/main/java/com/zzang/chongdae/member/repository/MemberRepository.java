package com.zzang.chongdae.member.repository;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByPassword(String password);

    boolean existsByPassword(String password);

    boolean existsByNickname(String nickname);

    Optional<MemberEntity> findByLoginId(String loginId);

    boolean existsByIdAndFcmToken(Long id, String fcmToken);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM MemberEntity m WHERE m.id = :id")
    Optional<MemberEntity> findByIdWithLock(Long id);
}
