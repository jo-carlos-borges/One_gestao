package br.com.one.records;

import br.com.one.domain.InstallmentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentResponse(
	    Long id,
	    BigDecimal amount,
	    LocalDate dueDate,
	    LocalDate paymentDate,
	    InstallmentStatus status,
	    String clientName, // Novo
	    String description // Novo
	) {
	    // Construtor de conveniência para manter compatibilidade com códigos antigos
	    public InstallmentResponse(Long id, BigDecimal amount, LocalDate dueDate, LocalDate paymentDate, InstallmentStatus status) {
	        this(id, amount, dueDate, paymentDate, status, null, null);
	    }
	}