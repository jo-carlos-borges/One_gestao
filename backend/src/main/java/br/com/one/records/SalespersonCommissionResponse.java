package br.com.one.records;

import java.math.BigDecimal;

public record SalespersonCommissionResponse(
		Long salespersonId,
		String salespersonName,
		BigDecimal totalComissionAmount
) {}
