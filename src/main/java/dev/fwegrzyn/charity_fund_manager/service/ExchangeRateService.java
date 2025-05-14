package dev.fwegrzyn.charity_fund_manager.service;

import dev.fwegrzyn.charity_fund_manager.dto.response.ExchangeApiDTO;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExchangeRateService {
    private final RestClient restClient;

    public ExchangeRateService(RestClient restClient) {
        this.restClient = restClient;
    }

    public Optional<Map<String, BigDecimal>> fetchLatestRates(String baseCurrency, List<String> currencyCodes) {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("EXCHANGE_API_KEY");
        try {
            ExchangeApiDTO dto = restClient.get()
                    .uri(uri -> uri
                            .path("/{key}/latest/{base}")
                            .build(apiKey, baseCurrency))
                    .retrieve()
                    .body(ExchangeApiDTO.class);

            return Optional.of(dto.getConversionRates().entrySet().stream()
                    .filter(e -> currencyCodes.contains(e.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        }
        catch (Exception ex) {
            return Optional.empty();
        }
    }

}

