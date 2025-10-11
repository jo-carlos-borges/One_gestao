package br.com.one.records;

import java.math.BigDecimal;

public record ProductSummaryResponse(Long id, String name, BigDecimal price) {}