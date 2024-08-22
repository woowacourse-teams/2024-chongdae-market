package com.zzang.chongdae.offeringmember.service.dto;

import java.util.List;

public record ParticipantResponse(ProposerResponseItem proposer,
                                  List<ParticipantResponseItem> participants,
                                  ParticipantCountResponseItem count,
                                  Integer price
) {
}
