package br.com.one.records;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import br.com.one.domain.PaymentMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SaleRequest (
		@NotNull Long clientId,
		@NotEmpty Set<Long> productIds,
		@NotNull PaymentMethod paymentMethod,
		
		BigDecimal downPayment,
		Integer numberOfInstallments,
		
		LocalDateTime baseInstallationDate,
		LocalDateTime siteInstallationDate,
		String validatorAccess,
		String vpsIp,
		Long installerId,
		boolean outOfHours
) {}
