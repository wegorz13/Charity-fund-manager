package dev.fwegrzyn.charity_fund_manager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("EXCHANGE_RATES")
public record ExchangeRate(
        @Id Integer id,
        Integer fromCurrencyId,
        Integer toCurrencyId,
        BigDecimal rate) {
}
