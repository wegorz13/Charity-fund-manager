package dev.fwegrzyn.charity_fund_manager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@Table("COLLECTION_BOXES")
public class CollectionBox {
    @Id
    private Integer id;
    private Integer eventId;
    @MappedCollection(idColumn = "BOX_ID")
    private Set<CollectionBoxBalance> balances;
}
