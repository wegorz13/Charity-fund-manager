package dev.fwegrzyn.charity_fund_manager.model;


import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("COLLECTION_BOX_BALANCES")
public class CollectionBoxBalance {
    private Integer currencyId;
    private BigDecimal amount;

    public CollectionBoxBalance() {}

    public CollectionBoxBalance(Integer currencyId, BigDecimal amount) {
        this.currencyId = currencyId;
        this.amount = amount;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

