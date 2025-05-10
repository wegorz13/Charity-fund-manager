package dev.fwegrzyn.charity_fund_manager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateFundraisingEventRequest(
        @NotBlank @Size(max = 255)
        String name,
        @NotBlank @Size(min = 3, max = 3)
        String currency){
}
