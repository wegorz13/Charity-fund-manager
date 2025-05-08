package dev.fwegrzyn.charity_fund_manager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateFundraisingEventRequest{
    @NotBlank @Size(max = 255)
    private String name;

    @NotBlank @Size(min = 3, max = 3)
    private String currency;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
