package dev.fwegrzyn.charity_fund_manager.config;

import dev.fwegrzyn.charity_fund_manager.model.Currency;
import dev.fwegrzyn.charity_fund_manager.model.ExchangeRate;
import dev.fwegrzyn.charity_fund_manager.repository.CurrencyRepository;
import dev.fwegrzyn.charity_fund_manager.repository.ExchangeRateRepository;
import dev.fwegrzyn.charity_fund_manager.service.ExchangeRateService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class DataLoader implements CommandLineRunner {
    private final DefaultDataProperties defaultDataProperties;
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateService exchangeRateService;

    public DataLoader(DefaultDataProperties defaultDataProperties,
                      CurrencyRepository currencyRepository,
                      ExchangeRateRepository exchangeRateRepository,
                      ExchangeRateService exchangeRateService) {
        this.defaultDataProperties = defaultDataProperties;
        this.currencyRepository = currencyRepository;
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        List<String> codes = defaultDataProperties.getCurrencies();
        codes.stream()
                .map(code -> new Currency(null, code))
                .forEach(currencyRepository::save);

        Map<String, Map<String, BigDecimal>> defaults = defaultDataProperties.getExchangeRates();
        for (String base : codes) {
            Map<String, BigDecimal> rates = exchangeRateService
                    .fetchLatestRates(base, codes)
                    .orElse(defaults.get(base));

            Integer baseId = currencyRepository.findByCode(base).orElseThrow().id();
            rates.forEach((targetCode, rate) -> {
                Integer targetId = currencyRepository.findByCode(targetCode).orElseThrow().id();
                exchangeRateRepository.save(new ExchangeRate(null, baseId, targetId, rate));
            });
        }
    }
}

