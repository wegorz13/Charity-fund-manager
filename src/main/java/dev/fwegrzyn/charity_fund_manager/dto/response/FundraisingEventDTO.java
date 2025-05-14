package dev.fwegrzyn.charity_fund_manager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class FundraisingEventDTO {
    private int id;
    private String name;
    private BigDecimal money;
    private String currency;
}
