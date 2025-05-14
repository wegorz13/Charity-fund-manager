package dev.fwegrzyn.charity_fund_manager.service;

import dev.fwegrzyn.charity_fund_manager.dto.response.CollectionBoxDTO;
import dev.fwegrzyn.charity_fund_manager.exception.BoxAlreadyAssignedException;
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

        CollectionBox box = new CollectionBox(null, null, balances);

        return collectionBoxRepository.save(box);
    }

    public List<CollectionBoxDTO> getBoxes(){
        List<CollectionBox> boxes = collectionBoxRepository.findAll();

        return boxes.stream().map((box) -> {
            boolean hasAnyMoney = box.getBalances().stream()
                    .anyMatch(balance -> balance.getAmount().compareTo(BigDecimal.ZERO) > 0);

            CollectionBoxDTO responseBox = new CollectionBoxDTO();

            responseBox.setAssigned(box.getEventId()!=null);
            responseBox.setEmpty(!hasAnyMoney);
            responseBox.setId(box.getId());

            return responseBox;
        }).toList();
    }

    public void deleteById(Integer boxId){
        CollectionBox box = collectionBoxRepository.findById(boxId).
                orElseThrow(() -> new ResourceNotFoundException("Box " + boxId + " not found"));
        collectionBoxRepository.delete(box);
    }

    public void assignBoxToEvent(Integer boxId, Integer eventId){
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box " + boxId + " not found"));

        if (box.getEventId() != null) {
            throw new BoxAlreadyAssignedException(boxId);
        }

        if (!fundraisingEventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Event " + eventId + " not found");
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
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box " + boxId + " not found"));

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
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box " + boxId + " not found"));

        Integer eventId = box.getEventId();

        FundraisingEvent event = fundraisingEventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event " + eventId + " not found"));

        Currency eventCurrency = currencyRepository.findById(event.getCurrencyId())
                .orElseThrow(() -> new ResourceNotFoundException("Currency " + event.getCurrencyId() + " not found"));

        List<ExchangeRate> ratesToEvent = exchangeRateRepository.findByToCurrencyId(eventCurrency.id());
        Map<Integer, BigDecimal> rateMap = ratesToEvent.stream()
                .collect(Collectors.toMap(ExchangeRate::fromCurrencyId, ExchangeRate::rate));

        BigDecimal total = BigDecimal.ZERO;

        for(CollectionBoxBalance balance: box.getBalances()) {
            Currency balanceCurrency = currencyRepository.findById(balance.getCurrencyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Currency " + balance.getCurrencyId() + " not found"));
    
            BigDecimal exchangeRate = rateMap.get(balanceCurrency.id());

            if (exchangeRate == null) {
                throw new ResourceNotFoundException(
                        String.format("No exchange rate from %s to %s", balance.getCurrencyId(), eventCurrency.code()));
            }

            total = total.add(balance.getAmount().multiply(exchangeRate));
            balance.setAmount(BigDecimal.ZERO);
        }

        event.setMoney(event.getMoney().add(total));
        collectionBoxRepository.save(box);
        fundraisingEventRepository.save(event);
    }
}
