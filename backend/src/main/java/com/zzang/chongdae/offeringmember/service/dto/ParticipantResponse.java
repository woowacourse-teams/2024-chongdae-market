package com.zzang.chongdae.offeringmember.service.dto;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.domain.OfferingMembers;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import java.util.List;

public record ParticipantResponse(ProposerResponseItem proposer,
                                  List<ParticipantResponseItem> participants,
                                  ParticipantCountResponseItem count,
                                  Integer price
) {

    public static ParticipantResponse from(OfferingEntity offering, OfferingMembers offeringMembers) {
        int pricePerOne = offering.toOfferingPrice().calculateDividedPrice();

        OfferingMemberEntity proposer = offeringMembers.getProposer();
        List<OfferingMemberEntity> participants = offeringMembers.getParticipants();
        ProposerResponseItem proposerResponse = new ProposerResponseItem(proposer, pricePerOne);
        List<ParticipantResponseItem> participantsResponse = participants.stream()
                .map(participant -> new ParticipantResponseItem(participant, pricePerOne))
                .toList();
        ParticipantCountResponseItem countResponse = new ParticipantCountResponseItem(offering);

        return new ParticipantResponse(proposerResponse, participantsResponse, countResponse, pricePerOne);
    }
}
