package br.com.one.records;

import br.com.one.domain.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record SubscriptionRequest(
        @NotNull Long clientId,
        @NotBlank String softwareName,

        // Dados da Implementação (Setup)
        @NotNull @DecimalMin("0.0") BigDecimal implementationFee,
        @NotNull PaymentMethod implementationPaymentMethod,
        Integer numberOfInstallments, // Opcional, se for parcelado

        // Dados da Mensalidade
        @NotNull @DecimalMin("0.0") BigDecimal monthlyFee,
        @NotNull Integer dueDay 
) {}