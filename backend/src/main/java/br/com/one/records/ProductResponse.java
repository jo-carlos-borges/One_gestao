package br.com.one.records;

import java.math.BigDecimal;

import br.com.one.domain.BusinessUnit;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        String description,
        BusinessUnit businessUnit) {
}
