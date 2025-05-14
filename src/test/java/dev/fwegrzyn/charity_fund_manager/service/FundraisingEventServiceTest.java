package dev.fwegrzyn.charity_fund_manager.service;


import dev.fwegrzyn.charity_fund_manager.dto.response.FundraisingEventDTO;
import dev.fwegrzyn.charity_fund_manager.exception.ResourceNotFoundException;
import dev.fwegrzyn.charity_fund_manager.model.Currency;
import dev.fwegrzyn.charity_fund_manager.model.FundraisingEvent;
import dev.fwegrzyn.charity_fund_manager.repository.CurrencyRepository;
import dev.fwegrzyn.charity_fund_manager.repository.FundraisingEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FundraisingEventServiceTest {

    @Mock
    private FundraisingEventRepository fundraisingEventRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private FundraisingEventService fundraisingEventService;

    private Currency testCurrency;
    private FundraisingEvent testEvent;

    @BeforeEach
    void setUp() {
        testCurrency = new Currency(1, "USD");
        testEvent = new FundraisingEvent(1, "Test Event", 1, BigDecimal.valueOf(100));
    }

    @Test
    void createEvent_WithValidData_ShouldReturnCreatedEvent() {
        // Given
        String eventName = "Test Event";
        String currencyCode = "USD";

        when(currencyRepository.findByCode(currencyCode)).thenReturn(Optional.of(testCurrency));
        when(fundraisingEventRepository.save(any(FundraisingEvent.class))).thenReturn(testEvent);

        // When
        FundraisingEvent result = fundraisingEventService.createEvent(eventName, currencyCode);

        // Then
        assertNotNull(result);
        assertEquals(testEvent.getId(), result.getId());
        assertEquals(testEvent.getName(), result.getName());
        assertEquals(testEvent.getCurrencyId(), result.getCurrencyId());

        verify(currencyRepository).findByCode(currencyCode);
        verify(fundraisingEventRepository).save(any(FundraisingEvent.class));
    }

    @Test
    void createEvent_WithInvalidCurrency_ShouldThrowException() {
        // Given
        String eventName = "Test Event";
        String currencyCode = "XYZ";

        when(currencyRepository.findByCode(currencyCode)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> fundraisingEventService.createEvent(eventName, currencyCode));

        assertEquals("Currency XYZ not found", exception.getMessage());
        verify(currencyRepository).findByCode(currencyCode);
        verify(fundraisingEventRepository, never()).save(any(FundraisingEvent.class));
    }

    @Test
    void getEventReports_ShouldReturnListOfEvents() {
        // Given
        FundraisingEvent event1 = new FundraisingEvent(1, "Event 1", 1, BigDecimal.valueOf(100));
        FundraisingEvent event2 = new FundraisingEvent(2, "Event 2", 2, BigDecimal.valueOf(200));
        List<FundraisingEvent> events = Arrays.asList(event1, event2);

        Currency currency1 = new Currency(1, "USD");
        Currency currency2 = new Currency(2, "EUR");

        when(fundraisingEventRepository.findAll()).thenReturn(events);
        when(currencyRepository.findById(1)).thenReturn(Optional.of(currency1));
        when(currencyRepository.findById(2)).thenReturn(Optional.of(currency2));

        // When
        List<FundraisingEventDTO> results = fundraisingEventService.getEventReports();

        // Then
        assertNotNull(results);
        assertEquals(2, results.size());

        assertEquals(1, results.get(0).getId());
        assertEquals("Event 1", results.get(0).getName());
        assertEquals(BigDecimal.valueOf(100), results.get(0).getMoney());
        assertEquals("USD", results.get(0).getCurrency());

        assertEquals(2, results.get(1).getId());
        assertEquals("Event 2", results.get(1).getName());
        assertEquals(BigDecimal.valueOf(200), results.get(1).getMoney());
        assertEquals("EUR", results.get(1).getCurrency());

        verify(fundraisingEventRepository).findAll();
        verify(currencyRepository).findById(1);
        verify(currencyRepository).findById(2);
    }

    @Test
    void getEventReports_WithMissingCurrency_ShouldUseUnknownCurrency() {
        // Given
        FundraisingEvent event = new FundraisingEvent(1, "Event 1", 999, BigDecimal.valueOf(100));
        List<FundraisingEvent> events = List.of(event);

        when(fundraisingEventRepository.findAll()).thenReturn(events);
        when(currencyRepository.findById(999)).thenReturn(Optional.empty());

        // When
        List<FundraisingEventDTO> results = fundraisingEventService.getEventReports();

        // Then
        assertNotNull(results);
        assertEquals(1, results.size());

        assertEquals(1, results.get(0).getId());
        assertEquals("Event 1", results.get(0).getName());
        assertEquals(BigDecimal.valueOf(100), results.get(0).getMoney());
        assertEquals("UNK", results.get(0).getCurrency());

        verify(fundraisingEventRepository).findAll();
        verify(currencyRepository).findById(999);
    }
}
