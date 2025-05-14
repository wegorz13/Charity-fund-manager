package dev.fwegrzyn.charity_fund_manager.dto.response;

import java.math.BigDecimal;

public class FundraisingEventDTO {
    private int id;
    private String name;
    private BigDecimal money;
    private String currency;

    public FundraisingEventDTO(int id, String name, BigDecimal amount, String currency) {
        this.id = id;
        this.name = name;
        this.money = amount;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
