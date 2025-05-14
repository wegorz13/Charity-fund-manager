package dev.fwegrzyn.charity_fund_manager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("CURRENCIES")
public record Currency(@Id Integer id, String code) {
}
