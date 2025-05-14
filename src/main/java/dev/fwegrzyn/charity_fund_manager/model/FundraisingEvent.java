package dev.fwegrzyn.charity_fund_manager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("FUNDRAISING_EVENTS")
public class FundraisingEvent {
    private @Id Integer id;
    private String name;
    private Integer currencyId;
    private BigDecimal money;

    public FundraisingEvent() {}

    public FundraisingEvent(Integer id, String name, Integer currencyId, BigDecimal money) {
        this.id = id;
        this.name = name;
        this.currencyId = currencyId;
        this.money = money;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }
}