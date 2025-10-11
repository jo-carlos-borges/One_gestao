package br.com.one.records;

import br.com.one.domain.InstallmentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentResponse(
        Long id,
        BigDecimal amount,
        LocalDate dueDate,
        LocalDate paymentDate,
        InstallmentStatus status
) {}