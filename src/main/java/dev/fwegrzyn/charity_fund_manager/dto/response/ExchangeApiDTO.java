package dev.fwegrzyn.charity_fund_manager.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public class ExchangeApiDTO {

    private String result;
    private String documentation;
    @JsonProperty("terms_of_use")
    private String termsOfUse;

    @JsonProperty("time_last_update_unix")
    private Long timeLastUpdateUnix;
    @JsonProperty("time_last_update_utc")
    private String timeLastUpdateUtc;
    @JsonProperty("time_next_update_unix")
    private Long timeNextUpdateUnix;
    @JsonProperty("time_next_update_utc")
    private String timeNextUpdateUtc;

    @JsonProperty("base_code")
    private String baseCode;

    @JsonProperty("conversion_rates")
    private Map<String, BigDecimal> conversionRates;


    public Map<String, BigDecimal> getConversionRates() {
        return conversionRates;
    }

}
