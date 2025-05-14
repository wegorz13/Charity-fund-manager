package dev.fwegrzyn.charity_fund_manager.service;

import dev.fwegrzyn.charity_fund_manager.dto.response.CollectionBoxDTO;
import dev.fwegrzyn.charity_fund_manager.exception.BoxAlreadyAssignedException;
import dev.fwegrzyn.charity_fund_manager.exception.BoxNotAssignedException;
import dev.fwegrzyn.charity_fund_manager.exception.BoxNotEmptyException;
import dev.fwegrzyn.charity_fund_manager.exception.ResourceNotFoundException;
import dev.fwegrzyn.charity_fund_manager.model.*;
import dev.fwegrzyn.charity_fund_manager.repository.CollectionBoxRepository;
import dev.fwegrzyn.charity_fund_manager.repository.CurrencyRepository;
import dev.fwegrzyn.charity_fund_manager.repository.ExchangeRateRepository;
import dev.fwegrzyn.charity_fund_manager.repository.FundraisingEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CollectionBoxService {
    private final CollectionBoxRepository collectionBoxRepository;
    private final CurrencyRepository currencyRepository;
    private final FundraisingEventRepository fundraisingEventRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    public CollectionBoxService(CollectionBoxRepository collectionBoxRepository, CurrencyRepository currencyRepository, FundraisingEventRepository fundraisingEventRepository, ExchangeRateRepository exchangeRateRepository) {
        this.collectionBoxRepository = collectionBoxRepository;
        this.currencyRepository = currencyRepository;
        this.fundraisingEventRepository = fundraisingEventRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Transactional
    public CollectionBox createBox(){
        Set<CollectionBoxBalance> balances = currencyRepository.findAll().stream()
                .map(c -> new CollectionBoxBalance(c.id(), BigDecimal.ZERO))
                .collect(Collectors.toSet());

        CollectionBox box = CollectionBox.builder().balances(balances).build();

        return collectionBoxRepository.save(box);
    }

    public List<CollectionBoxDTO> getBoxes() {
        return collectionBoxRepository.findAll().stream()
                .map(this::mapBoxToDto)
                .toList();
    }

    private CollectionBoxDTO mapBoxToDto(CollectionBox box) {
        boolean hasAnyMoney = box.getBalances().stream()
                .anyMatch(b -> b.getAmount().compareTo(BigDecimal.ZERO) > 0);

        return CollectionBoxDTO.builder()
                .id(box.getId())
                .assigned(box.getEventId() != null)
                .empty(!hasAnyMoney)
                .build();
    }

    public void deleteById(Integer boxId){
        CollectionBox box = getBoxOrThrow(boxId);
        collectionBoxRepository.delete(box);
    }

    public void assignBoxToEvent(Integer boxId, Integer eventId){
        CollectionBox box = getBoxOrThrow(boxId);

        if (box.getEventId() != null) {
            throw new BoxAlreadyAssignedException(boxId);
        }

        if (!fundraisingEventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Event with ID: " + eventId + " not found");
        }

        boolean hasMoney = box.getBalances().stream()
                .anyMatch(b -> b.getAmount().compareTo(BigDecimal.ZERO) > 0);

        if (hasMoney) {
            throw new BoxNotEmptyException(boxId);
        }

        box.setEventId(eventId);
        collectionBoxRepository.save(box);
    }

    public void donateMoneyToBox(Integer boxId, String currencyCode, BigDecimal amount) {
        CollectionBox box = getBoxOrThrow(boxId);

        Currency currency = currencyRepository.findByCode(currencyCode)
                .orElseThrow(() -> new ResourceNotFoundException("Currency " + currencyCode + " not found"));

        Integer currencyId = currency.id();

        CollectionBoxBalance balance = box.getBalances().stream()
                .filter(b -> b.getCurrencyId().equals(currencyId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No balance entry for box " + boxId + " and currency " + currencyCode));

        balance.setAmount(balance.getAmount().add(amount));

        collectionBoxRepository.save(box);
    }

    @Transactional
    public void transferMoneyToEvent(Integer boxId){
        CollectionBox box = getBoxOrThrow(boxId);

        Integer eventId = box.getEventId();

        if (eventId == null) {
            throw new BoxNotAssignedException(boxId);
        }

        FundraisingEvent event = getFundraisingEventOrThrow(eventId);

        Currency eventCurrency = getCurrencyOrThrow(event.getCurrencyId());

        List<ExchangeRate> ratesToEvent = exchangeRateRepository.findByToCurrencyId(eventCurrency.id());
        Map<Integer, BigDecimal> rateMap = ratesToEvent.stream()
                .collect(Collectors.toMap(ExchangeRate::fromCurrencyId, ExchangeRate::rate));

        BigDecimal total = calculateAndResetBalances(box, rateMap);

        event.setMoney(event.getMoney().add(total));
        collectionBoxRepository.save(box);
        fundraisingEventRepository.save(event);
    }

    private BigDecimal calculateAndResetBalances(CollectionBox box, Map<Integer, BigDecimal> rateMap) {
        BigDecimal total = BigDecimal.ZERO;

        for (CollectionBoxBalance balance : box.getBalances()) {
            BigDecimal rate = rateMap.get(balance.getCurrencyId());

            if (rate == null) {
                throw new ResourceNotFoundException(
                        String.format("Missing exchange rate from %s", balance.getCurrencyId()));
            }

            total = total.add(balance.getAmount().multiply(rate));
            balance.setAmount(BigDecimal.ZERO);
        }
        return total;
    }

    private CollectionBox getBoxOrThrow(Integer boxId){
        return collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box with ID: " + boxId + " not found"));
    }

    private Currency getCurrencyOrThrow(Integer currencyId){
        return currencyRepository.findById(currencyId)
                .orElseThrow(() -> new ResourceNotFoundException("Currency with ID: " + currencyId + " not found"));
    }

    private FundraisingEvent getFundraisingEventOrThrow(Integer eventId){
        return fundraisingEventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event with ID: " + eventId + " not found"));
    }
}
