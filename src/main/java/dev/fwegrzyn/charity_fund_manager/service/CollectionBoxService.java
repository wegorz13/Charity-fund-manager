package dev.fwegrzyn.charity_fund_manager.service;

import dev.fwegrzyn.charity_fund_manager.dto.response.CollectionBoxDTO;
import dev.fwegrzyn.charity_fund_manager.exception.BoxNotEmptyException;
import dev.fwegrzyn.charity_fund_manager.exception.ResourceNotFoundException;
import dev.fwegrzyn.charity_fund_manager.model.CollectionBox;
import dev.fwegrzyn.charity_fund_manager.model.CollectionBoxBalance;
import dev.fwegrzyn.charity_fund_manager.model.Currency;
import dev.fwegrzyn.charity_fund_manager.repository.CollectionBoxBalancesRepository;
import dev.fwegrzyn.charity_fund_manager.repository.CollectionBoxRepository;
import dev.fwegrzyn.charity_fund_manager.repository.CurrencyRepository;
import dev.fwegrzyn.charity_fund_manager.repository.FundraisingEventRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CollectionBoxService {
    private final CollectionBoxRepository collectionBoxRepository;
    private final CollectionBoxBalancesRepository collectionBoxBalancesRepository;
    private final CurrencyRepository currencyRepository;
    private final FundraisingEventRepository fundraisingEventRepository;

    public CollectionBoxService(CollectionBoxRepository collectionBoxRepository, CollectionBoxBalancesRepository collectionBoxBalancesRepository, CurrencyRepository currencyRepository, FundraisingEventRepository fundraisingEventRepository) {
        this.collectionBoxRepository = collectionBoxRepository;
        this.collectionBoxBalancesRepository = collectionBoxBalancesRepository;
        this.currencyRepository = currencyRepository;
        this.fundraisingEventRepository = fundraisingEventRepository;
    }

    public CollectionBox createBox(){
        CollectionBox createdBox = collectionBoxRepository.save(new CollectionBox(null, null));
        Integer boxId = createdBox.getId();

        List<Currency> allCurrencies = currencyRepository.findAll();

        List<CollectionBoxBalance> initialBalances = allCurrencies.stream()
                .map(cur -> new CollectionBoxBalance(
                        boxId,
                        cur.id(),
                        BigDecimal.ZERO
                ))
                .toList();

        collectionBoxBalancesRepository.saveAll(initialBalances);

        return createdBox;
    }

    public List<CollectionBoxDTO> getBoxes(){
        List<CollectionBox> boxes = collectionBoxRepository.findAll();

        List<CollectionBoxDTO> boxesToReturn = boxes.stream().map((box) -> {

            List<CollectionBoxBalance> balances = collectionBoxBalancesRepository.findByBoxId(box.getId());
            boolean hasAnyMoney = balances.stream()
                    .anyMatch(b -> b.getAmount().compareTo(BigDecimal.ZERO) > 0);

            CollectionBoxDTO responseBox = new CollectionBoxDTO();

            responseBox.setAssigned(box.getEventId()!=null);
            responseBox.setEmpty(!hasAnyMoney);
            responseBox.setId(box.getId());

            return responseBox;
        }).toList();

        return boxesToReturn;
    }

    public boolean deleteById(Integer boxId){
        if (!collectionBoxRepository.existsById(boxId)) {
            return false;
        }

        collectionBoxRepository.deleteById(boxId);

        return true;
    }

    public void assignBoxToEvent(Integer boxId, Integer eventId){
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box " + boxId + " not found"));

        if (!fundraisingEventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Event " + eventId + " not found");
        }

        List<CollectionBoxBalance> balances = collectionBoxBalancesRepository.findByBoxId(boxId);
        boolean hasMoney = balances.stream()
                .anyMatch(b -> b.getAmount().compareTo(BigDecimal.ZERO) > 0);

        if (hasMoney) {
            throw new BoxNotEmptyException(boxId);
        }

        box.setEventId(eventId);
        collectionBoxRepository.save(box);
    }

    public void donateMoneyToBox(Integer boxId, String currencyCode, BigDecimal amount){
        if (!collectionBoxRepository.existsById(boxId)) {
            throw new ResourceNotFoundException("Box " + boxId + " not found");
        }

        Optional<Currency> currency = currencyRepository.findByName(currencyCode);

        if (currency.isEmpty()) {
            throw new ResourceNotFoundException("Currency " + currencyCode + " not found");
        }

        Integer currencyId = currency.get().id();
        CollectionBoxBalance balance = collectionBoxBalancesRepository
                .findByCurrencyId(currencyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No balance entry for box " + boxId + " and currency " + currencyCode));

        balance.setAmount(balance.getAmount().add(amount));
        collectionBoxBalancesRepository.save(balance);
    }
}
