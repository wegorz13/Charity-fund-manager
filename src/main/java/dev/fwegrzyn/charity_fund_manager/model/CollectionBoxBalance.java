package dev.fwegrzyn.charity_fund_manager.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("COLLECTION_BOX_BALANCES")
public class CollectionBoxBalance {
    private Integer currencyId;
    private BigDecimal amount;
}