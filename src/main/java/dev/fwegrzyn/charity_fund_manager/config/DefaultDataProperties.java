package dev.fwegrzyn.charity_fund_manager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "app.default")
public class DefaultDataProperties {
    private List<String> currencies = new ArrayList<>();
    private Map<String, Map<String, BigDecimal>> exchangeRates = new HashMap<>();
}

