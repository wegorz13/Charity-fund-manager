package dev.fwegrzyn.charity_fund_manager.repository;

import dev.fwegrzyn.charity_fund_manager.model.FundraisingEvent;
import org.springframework.data.repository.CrudRepository;

public interface FundraisingEventRepository extends CrudRepository<FundraisingEvent, Integer> {
}
