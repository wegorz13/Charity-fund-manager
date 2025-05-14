package dev.fwegrzyn.charity_fund_manager.service;

import dev.fwegrzyn.charity_fund_manager.dto.response.ExchangeApiDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec<?> uriSpec;

    @Mock
    private RestClient.RequestHeadersSpec<?> headersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private ExchangeRateService service;

    private ExchangeApiDTO apiDto;

    @BeforeEach
    void setUp() {
        apiDto = new ExchangeApiDTO();
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.85"));
        rates.put("PLN", new BigDecimal("4.20"));
        rates.put("GBP", new BigDecimal("0.75"));
        apiDto.setExchangeRates(rates);
    }

    @Test
    void fetchLatestRates_ReturnsFilteredRates_WhenApiReturnsData() {
        // Given
        doReturn(uriSpec).when(restClient).get();
        doReturn(headersSpec).when(uriSpec).uri(any(Function.class));
        doReturn(responseSpec).when(headersSpec).retrieve();
        doReturn(apiDto).when(responseSpec).body(ExchangeApiDTO.class);

        // When
        Optional<Map<String, BigDecimal>> result = service.fetchLatestRates("USD", List.of("EUR", "PLN"));

        // Then
        assertTrue(result.isPresent(), "Result should be present when API returns data");
        Map<String, BigDecimal> filtered = result.get();
        assertEquals(2, filtered.size(), "Filtered map should contain exactly 2 entries");
        assertEquals(new BigDecimal("0.85"), filtered.get("EUR"));
        assertEquals(new BigDecimal("4.20"), filtered.get("PLN"));

        verify(restClient).get();
        verify(uriSpec).uri(any(Function.class));
        verify(headersSpec).retrieve();
        verify(responseSpec).body(ExchangeApiDTO.class);
    }

    @Test
    void fetchLatestRates_ReturnsEmpty_WhenRestClientThrowsException() {
        // Given
        doReturn(uriSpec).when(restClient).get();
        doReturn(headersSpec).when(uriSpec).uri(any(Function.class));
        doThrow(new RestClientException("API error"))
                .when(headersSpec).retrieve();

        // When
        Optional<Map<String, BigDecimal>> result =
                service.fetchLatestRates("USD", List.of("EUR", "PLN"));

        // Then
        assertFalse(result.isPresent(),
                "Result should be empty when RestClient throws exception");

        verify(restClient).get();
        verify(uriSpec).uri(any(Function.class));
        verify(headersSpec).retrieve();
    }
}
