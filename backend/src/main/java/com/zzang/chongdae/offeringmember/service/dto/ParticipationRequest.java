package com.zzang.chongdae.offeringmember.service.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;

public record ParticipationRequest(@NotNull
                                   Long offeringId,

                                   Integer participationCount) {

    @Override
    public Integer participationCount() {
        return Objects.requireNonNullElse(participationCount, 1);
    }
}
