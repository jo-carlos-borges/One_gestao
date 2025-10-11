package br.com.one.records;

import br.com.one.domain.ProjectStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProjectSummaryResponse(
        Long id,
        String name,
        String clientName,
        BigDecimal totalValue,
        ProjectStatus status,
        LocalDateTime creationDate,
        int developerCount
) {}