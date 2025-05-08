package dev.fwegrzyn.charity_fund_manager.repository;

import dev.fwegrzyn.charity_fund_manager.model.ExchangeRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeRateRepository
        extends CrudRepository<ExchangeRate, Integer> {

    Optional<ExchangeRate> findByFromCurrencyIdAndToCurrencyId(Integer fromId, Integer toId);
}
