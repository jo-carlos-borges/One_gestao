package br.com.one.records;

import br.com.one.domain.PaymentMethod;
import br.com.one.domain.ProjectStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        ClientResponse client,
        UserSummaryResponse salesperson,
        Set<ProjectDeveloperResponse> developers,
        BigDecimal totalValue,
        ProjectStatus status,
        PaymentMethod paymentMethod,
        LocalDateTime creationDate,
        List<InstallmentResponse> installments
) {}