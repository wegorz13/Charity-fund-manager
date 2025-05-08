package dev.fwegrzyn.charity_fund_manager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("FUNDRAISING_EVENTS")
public record FundraisingEvent(
        @Id Integer id,
        String name,
        Integer currencyId,
        BigDecimal money
) {}