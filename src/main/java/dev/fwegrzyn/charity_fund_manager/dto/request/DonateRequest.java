package dev.fwegrzyn.charity_fund_manager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DonateRequest(
        @NotNull
        Integer boxId,
        @NotBlank
        String currency,
        @NotNull @Positive
        BigDecimal amount) {
}

