package dev.fwegrzyn.charity_fund_manager.repository;

import dev.fwegrzyn.charity_fund_manager.model.CollectionBox;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CollectionBoxRepository extends CrudRepository<CollectionBox, Integer> {
    @NonNull
    @Override
    List<CollectionBox> findAll();
}
