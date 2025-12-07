package br.com.one.mappers;

import br.com.one.domain.Installment;
import br.com.one.domain.Subscription;
import br.com.one.records.InstallmentResponse;
import br.com.one.records.SubscriptionResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SubscriptionMapper {
    private SubscriptionMapper() {}

    public static SubscriptionResponse toResponse(Subscription subscription, List<Installment> installments) {
        return new SubscriptionResponse(
            subscription.getId(),
            subscription.getClient().getName(),
            subscription.getSoftwareName(),
            subscription.getImplementationFee(),
            subscription.getMonthlyFee(),
            subscription.getStatus(),
            subscription.getStartDate(),
            installments != null ? installments.stream()
                .map(i -> new InstallmentResponse(i.getId(), i.getAmount(), i.getDueDate(), i.getPaymentDate(), i.getStatus()))
                .collect(Collectors.toList()) : Collections.emptyList()
        );
    }
}