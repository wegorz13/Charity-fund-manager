package dev.fwegrzyn.charity_fund_manager.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectionBoxDTO {
    private Integer id;
    private boolean assigned;
    private boolean empty;
}
