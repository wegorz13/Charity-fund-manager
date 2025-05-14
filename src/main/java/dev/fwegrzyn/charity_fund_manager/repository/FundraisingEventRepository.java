package dev.fwegrzyn.charity_fund_manager.repository;

import dev.fwegrzyn.charity_fund_manager.model.FundraisingEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundraisingEventRepository extends CrudRepository<FundraisingEvent, Integer> {
    @Override @NonNull
    List<FundraisingEvent> findAll();


}
