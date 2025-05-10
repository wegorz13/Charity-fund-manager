package dev.fwegrzyn.charity_fund_manager.repository;

import dev.fwegrzyn.charity_fund_manager.model.CollectionBox;
import dev.fwegrzyn.charity_fund_manager.model.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Integer> {
    Optional<Currency> findByName(String name);

    @Override
    @NonNull
    List<Currency> findAll();
}
