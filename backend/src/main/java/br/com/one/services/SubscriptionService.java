package br.com.one.services;

import br.com.one.domain.*;
import br.com.one.mappers.SubscriptionMapper;
import br.com.one.records.SubscriptionRequest;
import br.com.one.records.SubscriptionResponse;
import br.com.one.repositories.ClientRepository;
import br.com.one.repositories.InstallmentRepository;
import br.com.one.repositories.SubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ClientRepository clientRepository;
    private final InstallmentRepository installmentRepository;

    @Transactional
    public SubscriptionResponse createSubscription(SubscriptionRequest request, User salesperson) {
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + request.clientId()));

        Subscription subscription = Subscription.builder()
                .client(client)
                .salesperson(salesperson)
                .softwareName(request.softwareName())
                .implementationFee(request.implementationFee())
                .implementationPaymentMethod(request.implementationPaymentMethod())
                .monthlyFee(request.monthlyFee())
                .dueDay(request.dueDay())
                .status(SubscriptionStatus.IMPLEMENTATION)
                .startDate(LocalDateTime.now())
                .build();

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        List<Installment> installments = new ArrayList<>();

        if (request.implementationFee().compareTo(BigDecimal.ZERO) > 0) {
            
            if (request.implementationPaymentMethod() == PaymentMethod.INSTALLMENTS_PLAN
                && request.numberOfInstallments() != null 
                && request.numberOfInstallments() > 0) {

                BigDecimal totalFee = request.implementationFee();
                int parcels = request.numberOfInstallments();
                
                BigDecimal installmentValue = totalFee.divide(BigDecimal.valueOf(parcels), 2, RoundingMode.FLOOR);
                
                BigDecimal remainder = totalFee.subtract(installmentValue.multiply(BigDecimal.valueOf(parcels)));

                for (int i = 1; i <= parcels; i++) {
                    BigDecimal currentAmount = installmentValue;
                    
                    if (i == 1) {
                        currentAmount = currentAmount.add(remainder);
                    }

                    installments.add(Installment.builder()
                            .subscription(savedSubscription)
                            .amount(currentAmount)
                            .dueDate(LocalDate.now().plusMonths(i))
                            .status(InstallmentStatus.PENDING)
                            .commissionPaid(false)
                            .build());
                }
            } 
            else {
                installments.add(Installment.builder()
                        .subscription(savedSubscription)
                        .amount(request.implementationFee())
                        .dueDate(LocalDate.now().plusDays(1))
                        .status(InstallmentStatus.PENDING)
                        .commissionPaid(false)
                        .build());
            }
            
            if (!installments.isEmpty()) {
                installmentRepository.saveAll(installments);
            }
        }

        return SubscriptionMapper.toResponse(savedSubscription, installments);
    }

    public List<SubscriptionResponse> findAll() {
        return subscriptionRepository.findAll().stream()
                .map(s -> SubscriptionMapper.toResponse(s, null))
                .toList();
    }
}