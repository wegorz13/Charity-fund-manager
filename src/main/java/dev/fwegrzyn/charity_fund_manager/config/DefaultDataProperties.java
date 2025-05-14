package dev.fwegrzyn.charity_fund_manager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "app.default")
public class DefaultDataProperties {

    private List<String> currencies = new ArrayList<>();
    private Map<String, Map<String, BigDecimal>> exchangeRates = new HashMap<>();

    public List<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<String> currencies) {
        this.currencies = currencies;
    }

    public Map<String, Map<String, BigDecimal>> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(Map<String, Map<String, BigDecimal>> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }
}

