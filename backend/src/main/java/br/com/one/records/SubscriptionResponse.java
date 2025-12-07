package br.com.one.records;

import br.com.one.domain.SubscriptionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SubscriptionResponse(
        Long id,
        String clientName,
        String softwareName,
        BigDecimal implementationFee,
        BigDecimal monthlyFee,
        SubscriptionStatus status,
        LocalDateTime startDate,
        List<InstallmentResponse> implementationInstallments
) {}