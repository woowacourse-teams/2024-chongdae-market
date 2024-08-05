package com.zzang.chongdae.auth.service.dto;

import jakarta.validation.constraints.NotNull;

public record SignupRequest(@NotNull String ci) {
}
