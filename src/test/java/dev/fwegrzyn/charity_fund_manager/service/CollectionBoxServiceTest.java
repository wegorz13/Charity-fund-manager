package dev.fwegrzyn.charity_fund_manager.service;

import dev.fwegrzyn.charity_fund_manager.dto.response.CollectionBoxDTO;
import dev.fwegrzyn.charity_fund_manager.exception.BoxAlreadyAssignedException;
import dev.fwegrzyn.charity_fund_manager.exception.ResourceNotFoundException;
import dev.fwegrzyn.charity_fund_manager.model.*;
import dev.fwegrzyn.charity_fund_manager.model.Currency;
import dev.fwegrzyn.charity_fund_manager.repository.CollectionBoxRepository;
import dev.fwegrzyn.charity_fund_manager.repository.CurrencyRepository;
import dev.fwegrzyn.charity_fund_manager.repository.ExchangeRateRepository;
import dev.fwegrzyn.charity_fund_manager.repository.FundraisingEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CollectionBoxServiceTest {

    @Mock
    private CollectionBoxRepository collectionBoxRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private FundraisingEventRepository fundraisingEventRepository;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private CollectionBoxService collectionBoxService;

    private Currency usdCurrency;
    private Currency eurCurrency;
    private FundraisingEvent testEvent;
    private CollectionBox testBox;
    private Set<CollectionBoxBalance> balances;
    private List<Currency> allCurrencies;
    private ExchangeRate usdToEurRate;

    @BeforeEach
    void setUp() {
        usdCurrency = new Currency(1, "USD");
        eurCurrency = new Currency(2, "EUR");
        allCurrencies = Arrays.asList(usdCurrency, eurCurrency);

        balances = new HashSet<>();
        balances.add(new CollectionBoxBalance(1, BigDecimal.ZERO));
        balances.add(new CollectionBoxBalance(2, BigDecimal.ZERO));

        testBox = new CollectionBox(1, null, balances);
        testEvent = new FundraisingEvent(1, "Charity Gala", 2, BigDecimal.ZERO);

        usdToEurRate = new ExchangeRate(1, 1, 2, new BigDecimal("0.85"));
    }

    @Test
    void createBox_ShouldCreateBoxWithZeroBalancesForAllCurrencies() {
        // Given
        when(currencyRepository.findAll()).thenReturn(allCurrencies);
        when(collectionBoxRepository.save(any(CollectionBox.class))).thenAnswer(invocation -> {
            CollectionBox box = invocation.getArgument(0);
            box.setId(1);
            return box;
        });

        // When
        CollectionBox result = collectionBoxService.createBox();

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertNull(result.getEventId());
        assertEquals(2, result.getBalances().size());

        // Verify all currencies have balances
        Set<Integer> balanceCurrencyIds = result.getBalances().stream()
                .map(CollectionBoxBalance::getCurrencyId)
                .collect(Collectors.toSet());
        assertTrue(balanceCurrencyIds.contains(1));
        assertTrue(balanceCurrencyIds.contains(2));

        // Verify all balances are zero
        result.getBalances().forEach(balance ->
                assertEquals(0, balance.getAmount().compareTo(BigDecimal.ZERO)));

        verify(currencyRepository).findAll();
        verify(collectionBoxRepository).save(any(CollectionBox.class));
    }

    @Test
    void getBoxes_ShouldReturnBoxDTOList() {
        // Given
        List<CollectionBox> boxes = List.of(testBox);
        when(collectionBoxRepository.findAll()).thenReturn(boxes);

        // When
        List<CollectionBoxDTO> results = collectionBoxService.getBoxes();

        // Then
        assertNotNull(results);
        assertEquals(1, results.size());

        CollectionBoxDTO dto = results.get(0);
        assertEquals(1, dto.getId());
        assertFalse(dto.isAssigned());
        assertTrue(dto.isEmpty());

        verify(collectionBoxRepository).findAll();
    }

    @Test
    void deleteById_WithExistingId_ShouldDeleteBox() {
        // Given
        Integer boxId = 1;
        when(collectionBoxRepository.findById(boxId)).thenReturn(Optional.of(testBox));
        doNothing().when(collectionBoxRepository).delete(any(CollectionBox.class));

        // When
        collectionBoxService.deleteById(boxId);

        // Then
        verify(collectionBoxRepository).findById(boxId);
        verify(collectionBoxRepository).delete(testBox);
    }

    @Test
    void deleteById_WithNonExistingId_ShouldThrowException() {
        // Given
        Integer boxId = 999;
        when(collectionBoxRepository.findById(boxId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> collectionBoxService.deleteById(boxId));

        assertEquals("Box with ID: 999 not found", exception.getMessage());
        verify(collectionBoxRepository).findById(boxId);
        verify(collectionBoxRepository, never()).delete(any(CollectionBox.class));
    }

    @Test
    void assignBoxToEvent_WithValidData_ShouldAssignBox() {
        // Given
        Integer boxId = 1;
        Integer eventId = 1;

        when(collectionBoxRepository.findById(boxId)).thenReturn(Optional.of(testBox));
        when(fundraisingEventRepository.existsById(eventId)).thenReturn(true);
        when(collectionBoxRepository.save(any(CollectionBox.class))).thenReturn(testBox);

        // When
        collectionBoxService.assignBoxToEvent(boxId, eventId);

        // Then
        ArgumentCaptor<CollectionBox> boxCaptor = ArgumentCaptor.forClass(CollectionBox.class);
        verify(collectionBoxRepository).save(boxCaptor.capture());

        CollectionBox savedBox = boxCaptor.getValue();
        assertEquals(eventId, savedBox.getEventId());

        verify(collectionBoxRepository).findById(boxId);
        verify(fundraisingEventRepository).existsById(eventId);
    }

    @Test
    void assignBoxToEvent_WithNonExistingBox_ShouldThrowException() {
        // Given
        Integer boxId = 999;
        Integer eventId = 1;

        when(collectionBoxRepository.findById(boxId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> collectionBoxService.assignBoxToEvent(boxId, eventId));

        assertEquals("Box with ID: 999 not found", exception.getMessage());
        verify(collectionBoxRepository).findById(boxId);
        verify(fundraisingEventRepository, never()).existsById(anyInt());
    }

    @Test
    void assignBoxToEvent_WithNonExistingEvent_ShouldThrowException() {
        // Given
        Integer boxId = 1;
        Integer eventId = 999;

        when(collectionBoxRepository.findById(boxId)).thenReturn(Optional.of(testBox));
        when(fundraisingEventRepository.existsById(eventId)).thenReturn(false);

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> collectionBoxService.assignBoxToEvent(boxId, eventId));

        assertEquals("Event with ID: 999 not found", exception.getMessage());
        verify(collectionBoxRepository).findById(boxId);
        verify(fundraisingEventRepository).existsById(eventId);
    }

    @Test
    void assignBoxToEvent_WithAlreadyAssignedBox_ShouldThrowException() {
        // Given
        Integer boxId = 1;
        Integer eventId = 2;
        CollectionBox assignedBox = new CollectionBox(1, 2, balances); // already assigned to event 2

        when(collectionBoxRepository.findById(boxId)).thenReturn(Optional.of(assignedBox));

        // When & Then
        BoxAlreadyAssignedException exception = assertThrows(BoxAlreadyAssignedException.class,
                () -> collectionBoxService.assignBoxToEvent(boxId, eventId));

        verify(collectionBoxRepository).findById(boxId);
        verify(fundraisingEventRepository, never()).existsById(anyInt());
    }

    @Test
    void donateMoneyToBox_WithValidData_ShouldUpdateBalance() {
        // Given
        Integer boxId = 1;
        String currencyCode = "USD";
        BigDecimal amount = new BigDecimal("50.00");

        when(collectionBoxRepository.findById(boxId)).thenReturn(Optional.of(testBox));
        when(currencyRepository.findByCode(currencyCode)).thenReturn(Optional.of(usdCurrency));
        when(collectionBoxRepository.save(any(CollectionBox.class))).thenReturn(testBox);

        // When
        collectionBoxService.donateMoneyToBox(boxId, currencyCode, amount);

        // Then
        ArgumentCaptor<CollectionBox> boxCaptor = ArgumentCaptor.forClass(CollectionBox.class);
        verify(collectionBoxRepository).save(boxCaptor.capture());

        CollectionBox savedBox = boxCaptor.getValue();
        Optional<CollectionBoxBalance> updatedBalance = savedBox.getBalances().stream()
                .filter(b -> b.getCurrencyId().equals(usdCurrency.id()))
                .findFirst();

        assertTrue(updatedBalance.isPresent());
        assertEquals(0, updatedBalance.get().getAmount().compareTo(amount));

        verify(collectionBoxRepository).findById(boxId);
        verify(currencyRepository).findByCode(currencyCode);
    }

    @Test
    void transferMoneyToEvent_WithValidData_ShouldTransferMoney() {
        // Given
        Integer boxId = 1;
        Integer eventId = 1;

        Set<CollectionBoxBalance> nonZeroBalances = new HashSet<>();
        nonZeroBalances.add(new CollectionBoxBalance(usdCurrency.id(), new BigDecimal("100.00")));
        CollectionBox boxWithMoney = new CollectionBox(boxId, eventId, nonZeroBalances);

        when(collectionBoxRepository.findById(boxId)).thenReturn(Optional.of(boxWithMoney));
        when(fundraisingEventRepository.findById(eventId)).thenReturn(Optional.of(testEvent));
        when(currencyRepository.findById(testEvent.getCurrencyId())).thenReturn(Optional.of(eurCurrency));

        List<ExchangeRate> rates = List.of(usdToEurRate);
        when(exchangeRateRepository.findByToCurrencyId(eurCurrency.id())).thenReturn(rates);


        // When
        collectionBoxService.transferMoneyToEvent(boxId);

        // Then
        ArgumentCaptor<CollectionBox> boxCaptor = ArgumentCaptor.forClass(CollectionBox.class);
        verify(collectionBoxRepository).save(boxCaptor.capture());

        CollectionBox savedBox = boxCaptor.getValue();
        savedBox.getBalances().forEach(balance ->
                assertEquals(0, balance.getAmount().compareTo(BigDecimal.ZERO)));

        ArgumentCaptor<FundraisingEvent> eventCaptor = ArgumentCaptor.forClass(FundraisingEvent.class);
        verify(fundraisingEventRepository).save(eventCaptor.capture());

        FundraisingEvent savedEvent = eventCaptor.getValue();
        assertEquals(0, savedEvent.getMoney().compareTo(new BigDecimal("85.00")));

        verify(collectionBoxRepository).findById(boxId);
        verify(fundraisingEventRepository).findById(eventId);
        verify(exchangeRateRepository).findByToCurrencyId(eurCurrency.id());
    }
}