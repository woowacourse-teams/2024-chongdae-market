package com.zzang.chongdae.auth.service.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull String ci) {
}
