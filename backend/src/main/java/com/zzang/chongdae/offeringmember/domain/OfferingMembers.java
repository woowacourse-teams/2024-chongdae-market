package com.zzang.chongdae.offeringmember.domain;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offeringmember.exception.OfferingMemberErrorCode;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OfferingMembers {

    private final List<OfferingMemberEntity> offeringMembers;

    public MemberEntity getProposer() {
        return offeringMembers.stream()
                .filter(OfferingMemberEntity::isProposer)
                .findFirst()
                .orElseThrow(() -> new MarketException(OfferingMemberErrorCode.PROPOSER_NOT_FOUND))
                .getMember();
    }

    public List<MemberEntity> getParticipants() {
        return offeringMembers.stream()
                .filter(OfferingMemberEntity::isParticipant)
                .map(OfferingMemberEntity::getMember)
                .toList();
    }
}
