package br.com.one.mappers;

import br.com.one.domain.Installment;
import br.com.one.records.InstallmentResponse;

public class InstallmentMapper {

    private InstallmentMapper() {}

    public static InstallmentResponse toResponse(Installment installment) {
        if (installment == null) {
            return null;
        }

        String clientName = "Desconhecido";
        
        if (installment.getSubscription() != null) {
            clientName = installment.getSubscription().getClient().getName();
        } else if (installment.getSale() != null) {
            clientName = installment.getSale().getClient().getName();
        } else if (installment.getProject() != null) {
            clientName = installment.getProject().getClient().getName();
        }

        String description = installment.getDescription();
        if (description == null || description.isBlank()) {
            description = "Parcela";
        }

        return new InstallmentResponse(
            installment.getId(),
            installment.getAmount(),
            installment.getDueDate(),
            installment.getPaymentDate(),
            installment.getStatus(),
            clientName,
            description
        );
    }
}