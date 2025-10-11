package br.com.one.records;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.one.domain.PaymentMethod;

public record SaleSummaryResponse(
        Long id,
        String clientName,
        String salespersonName,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        LocalDateTime saleDate
) {}
