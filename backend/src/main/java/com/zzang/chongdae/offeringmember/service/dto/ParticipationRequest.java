package com.zzang.chongdae.offeringmember.service.dto;

import jakarta.validation.constraints.NotNull;

public record ParticipationRequest(@NotNull
                                   Long memberId,

                                   @NotNull
                                   Long offeringId) {
}
