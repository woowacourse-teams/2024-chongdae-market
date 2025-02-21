package com.zzang.chongdae.member.service;

import com.zzang.chongdae.global.config.WriterDatabase;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.exception.MemberErrorCode;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.member.service.dto.NicknameRequest;
import com.zzang.chongdae.member.service.dto.NicknameResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    @PersistenceContext
    private EntityManager entityManager;

    private final MemberRepository memberRepository;

    @WriterDatabase
    @Transactional
    public NicknameResponse changeNickname(MemberEntity member, NicknameRequest request) {
        MemberEntity targetMember = memberRepository.findByIdWithLock(member.getId())
                .orElseThrow(() -> new MarketException(MemberErrorCode.NOT_FOUND));
        String nickname = request.nickname();

        if (memberRepository.existsByNickname(nickname)) {
            throw new MarketException(MemberErrorCode.NICK_NAME_ALREADY_EXIST);
        }

        try {
            targetMember.updateNickName(nickname);
            entityManager.flush();
        } catch (ConstraintViolationException e) {
            throw new MarketException(MemberErrorCode.NICK_NAME_ALREADY_EXIST);
        }

        return new NicknameResponse(nickname);
    }

}
