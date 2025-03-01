package com.zzang.chongdae.offeringmember.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ParticipationRequest(@NotNull
                                   Long offeringId,

                                   @NotNull
                                   @Positive
                                   Integer participationCount) {
}
