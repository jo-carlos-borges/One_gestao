package br.com.one.records;

import br.com.one.domain.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

public record ProjectRequest(
        @NotBlank String name,
        String description,
        @NotNull Long clientId,
        @NotEmpty Set<Long> developerIds,
        @NotNull @DecimalMin("0.0") BigDecimal totalValue,
        @NotNull PaymentMethod paymentMethod,

        Integer numberOfInstallments
) {}