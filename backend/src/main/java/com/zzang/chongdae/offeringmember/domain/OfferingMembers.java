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

    public void validateParticipants(MemberEntity member) {
        offeringMembers.stream()
                .filter(offeringMember -> offeringMember.isSameMember(member))
                .findFirst()
                .orElseThrow(() -> new MarketException(OfferingMemberErrorCode.PARTICIPANT_NOT_FOUND));
    }

    public MemberEntity getProposer() {
        return offeringMembers.stream()
                .filter(OfferingMemberEntity::isProposer)
                .findFirst()
                .map(OfferingMemberEntity::getMember)
                .orElseThrow(() -> new MarketException(OfferingMemberErrorCode.PARTICIPANT_NOT_FOUND));
    }

    public List<MemberEntity> getParticipants() {
        return offeringMembers.stream()
                .filter(offeringMemberEntity -> !offeringMemberEntity.isProposer())
                .toList();
    }
}
