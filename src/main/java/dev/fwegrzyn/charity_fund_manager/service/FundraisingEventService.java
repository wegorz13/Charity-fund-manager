package dev.fwegrzyn.charity_fund_manager.service;

import dev.fwegrzyn.charity_fund_manager.dto.response.FundraisingEventDTO;
import dev.fwegrzyn.charity_fund_manager.exception.ResourceNotFoundException;
import dev.fwegrzyn.charity_fund_manager.model.Currency;
import dev.fwegrzyn.charity_fund_manager.model.FundraisingEvent;
import dev.fwegrzyn.charity_fund_manager.repository.CurrencyRepository;
import dev.fwegrzyn.charity_fund_manager.repository.FundraisingEventRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FundraisingEventService {
    private final FundraisingEventRepository fundraisingEventRepository;
    private final CurrencyRepository currencyRepository;

    public FundraisingEventService(FundraisingEventRepository fundraisingEventRepository, CurrencyRepository currencyRepository) {
        this.fundraisingEventRepository = fundraisingEventRepository;
        this.currencyRepository = currencyRepository;
    }

    public FundraisingEvent createEvent(String eventName, String currencyCode) {
        Optional<Currency> currency = currencyRepository.findByCode(currencyCode);

        if (currency.isEmpty()) {
            throw new ResourceNotFoundException("Currency " + currencyCode + " not found");
        }

        FundraisingEvent event = FundraisingEvent.builder().name(eventName).currencyId(currency.get().id()).money(BigDecimal.ZERO).build();

        return fundraisingEventRepository.save(event);
    }

    public List<FundraisingEventDTO> getEventReports(){
        List<FundraisingEvent> events = fundraisingEventRepository.findAll();
        Currency unknown = new Currency(0, "UNK");

        return events.stream().map((event) ->{
            Currency currency = currencyRepository.findById(event.getCurrencyId()).orElse(unknown);
            return new FundraisingEventDTO(event.getId(), event.getName(), event.getMoney(), currency.code());
        }).toList();
    }
}
