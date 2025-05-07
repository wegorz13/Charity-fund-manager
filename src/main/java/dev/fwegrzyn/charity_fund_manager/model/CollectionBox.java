package dev.fwegrzyn.charity_fund_manager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("COLLECTION_BOXES")
public record CollectionBox(@Id Integer id, Integer eventId, BigDecimal money) {}
