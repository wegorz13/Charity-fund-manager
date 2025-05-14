package dev.fwegrzyn.charity_fund_manager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;


@Table("COLLECTION_BOXES")
public class CollectionBox {
    @Id
    private Integer id;
    private Integer eventId;
    @MappedCollection(idColumn = "BOX_ID")
    private Set<CollectionBoxBalance> balances;

    public CollectionBox(Integer id, Integer eventId, Set<CollectionBoxBalance> balances) {
        this.id = id;
        this.eventId = eventId;
        this.balances = balances;
    }

    public CollectionBox() {}

    public Integer getId() {
        return id;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Set<CollectionBoxBalance> getBalances() {
        return balances;
    }
    public void setBalances(Set<CollectionBoxBalance> balances) {
        this.balances = balances;
    }
}
