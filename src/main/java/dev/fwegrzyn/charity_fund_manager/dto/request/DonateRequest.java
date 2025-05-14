package dev.fwegrzyn.charity_fund_manager.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DonateRequest(
        @NotNull @JsonProperty("box_id")
        Integer boxId,
        @NotBlank @Size(min = 3, max = 3)
        String currency,
        @NotNull @Positive
        BigDecimal amount) {
}

