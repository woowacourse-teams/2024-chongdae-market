package com.zzang.chongdae.member.repository;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
}
