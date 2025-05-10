package dev.fwegrzyn.charity_fund_manager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("COLLECTION_BOXES")
public class CollectionBox {
    @Id
    private Integer id;
    private Integer eventId;

    public CollectionBox(Integer id, Integer eventId) {
        this.id = id;
        this.eventId = eventId;
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
}
