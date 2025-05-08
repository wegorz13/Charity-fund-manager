package dev.fwegrzyn.charity_fund_manager.service;

import dev.fwegrzyn.charity_fund_manager.model.Currency;
import dev.fwegrzyn.charity_fund_manager.model.FundraisingEvent;
import dev.fwegrzyn.charity_fund_manager.repository.CurrencyRepository;
import dev.fwegrzyn.charity_fund_manager.repository.FundraisingEventRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class FundraisingEventService {
    private final FundraisingEventRepository fundraisingEventRepository;
    private final CurrencyRepository currencyRepository;

    public FundraisingEventService(FundraisingEventRepository fundraisingEventRepository, CurrencyRepository currencyRepository) {
        this.fundraisingEventRepository = fundraisingEventRepository;
        this.currencyRepository = currencyRepository;
    }

    public Optional<FundraisingEvent> createEvent(String eventName, String currencyCode) {
        Optional<Currency> currency = currencyRepository.findByName(currencyCode);

        if (currency.isEmpty()) {
            return Optional.empty();
        }

        FundraisingEvent event = new FundraisingEvent(null, eventName, currency.get().id(), BigDecimal.ZERO);
        return Optional.of(fundraisingEventRepository.save(event));
    }
}
