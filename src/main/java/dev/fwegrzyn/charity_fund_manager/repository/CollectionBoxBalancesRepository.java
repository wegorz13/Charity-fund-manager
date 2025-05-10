package dev.fwegrzyn.charity_fund_manager.repository;

import dev.fwegrzyn.charity_fund_manager.model.CollectionBoxBalance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionBoxBalancesRepository extends CrudRepository<CollectionBoxBalance, Integer> {

    List<CollectionBoxBalance> findByBoxId(Integer boxId);

    Optional<CollectionBoxBalance> findByCurrencyId(Integer currencyId);
}
