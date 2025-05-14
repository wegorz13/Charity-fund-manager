package dev.fwegrzyn.charity_fund_manager.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("FUNDRAISING_EVENTS")
public class FundraisingEvent {
    @Id private Integer id;
    private String name;
    private Integer currencyId;
    private BigDecimal money;
}