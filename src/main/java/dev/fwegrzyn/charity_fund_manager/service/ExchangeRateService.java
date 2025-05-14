package dev.fwegrzyn.charity_fund_manager.service;

import dev.fwegrzyn.charity_fund_manager.dto.response.ExchangeApiDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExchangeRateService {
    private final RestClient restClient;
    private static final Logger log = LoggerFactory.getLogger(ExchangeRateService.class);
    @Value("${exchange.api.key}")
    private String apiKey;

    public ExchangeRateService(RestClient restClient) {
        this.restClient = restClient;
    }

    public Optional<Map<String, BigDecimal>> fetchLatestRates(String baseCurrency, List<String> currencyCodes) {
        try {
            ExchangeApiDTO dto = restClient.get()
                    .uri(uri -> uri
                            .path("/{key}/latest/{base}")
                            .build(apiKey, baseCurrency))
                    .retrieve()
                    .body(ExchangeApiDTO.class);


            if (dto == null) {
                log.warn("External API returned null conversion rates for base {}", baseCurrency);
                return Optional.empty();
            }

            Map<String, BigDecimal> rates = dto.getExchangeRates();

            Map<String, BigDecimal> filteredRates = rates.entrySet().stream()
                    .filter(e -> currencyCodes.contains(e.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            return Optional.of(filteredRates);
        }
        catch (RestClientException ex) {
            log.error("External API error: {}", ex.getMessage(), ex);
            return Optional.empty();
        }
    }
}

