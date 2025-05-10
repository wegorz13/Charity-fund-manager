package dev.fwegrzyn.charity_fund_manager.model;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("COLLECTION_BOX_BALANCES")
public class CollectionBoxBalance{
    @Column("box_id")
    private Integer boxId;
    @Column("currency_id")
    private Integer currencyId;
    private BigDecimal amount;

    public CollectionBoxBalance() {}

    public CollectionBoxBalance(Integer boxId, Integer currencyId, BigDecimal amount) {
        this.boxId = boxId;
        this.currencyId = currencyId;
        this.amount = amount;
    }

    public Integer getBoxId() {
        return boxId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

