package com.zzang.chongdae.offeringmember.service.dto;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.domain.OfferingMembers;
import java.util.List;

public record ParticipantResponse(ProposerResponseItem proposer,
                                  List<ParticipantResponseItem> participants,
                                  ParticipantCountResponseItem count,
                                  Integer price
) {

    public static ParticipantResponse from(OfferingEntity offering, OfferingMembers offeringMembers) {
        MemberEntity proposer = offeringMembers.getProposer();
        List<MemberEntity> participants = offeringMembers.getParticipants();
        ProposerResponseItem proposerResponse = new ProposerResponseItem(proposer);
        List<ParticipantResponseItem> participantsResponse = participants.stream()
                .map(ParticipantResponseItem::new)
                .toList();
        ParticipantCountResponseItem countResponse = new ParticipantCountResponseItem(offering);
        Integer priceResponse = offering.toOfferingPrice().calculateDividedPrice();
        return new ParticipantResponse(proposerResponse, participantsResponse, countResponse, priceResponse);
    }
}
