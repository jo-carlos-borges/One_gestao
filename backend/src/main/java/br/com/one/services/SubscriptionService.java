package br.com.one.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.one.domain.Client;
import br.com.one.domain.Installment;
import br.com.one.domain.InstallmentStatus;
import br.com.one.domain.PaymentMethod;
import br.com.one.domain.Subscription;
import br.com.one.domain.SubscriptionStatus;
import br.com.one.domain.User;
import br.com.one.mappers.InstallmentMapper;
import br.com.one.mappers.SubscriptionMapper;
import br.com.one.records.InstallmentResponse;
import br.com.one.records.SubscriptionRequest;
import br.com.one.records.SubscriptionResponse;
import br.com.one.repositories.ClientRepository;
import br.com.one.repositories.InstallmentRepository;
import br.com.one.repositories.SubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

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

    public List<SubscriptionResponse> findAll(SubscriptionStatus status) {
        List<Subscription> subscriptions;

        if (status != null) {
            subscriptions = subscriptionRepository.findByStatus(status);
        } else {
            subscriptions = subscriptionRepository.findAll();
        }

        return subscriptions.stream()
                .map(s -> SubscriptionMapper.toResponse(s, null))
                .toList();
    }

    public SubscriptionResponse findById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assinatura não encontrada"));

        List<Installment> installments = installmentRepository.findBySubscriptionId(id);

        return SubscriptionMapper.toResponse(subscription, installments);
    }
    
    @Transactional
    public void activateSubscription(Long id) {
    	Subscription subscription = subscriptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Assinatura não encontrada"));
    	
    	subscription.setStatus(SubscriptionStatus.ACTIVE);
    	subscriptionRepository.save(subscription);
    			
    }
    
    @Transactional
    public int generateMonthlyInvoicesForActiveClients() {

    	List<Subscription> activeSubscriptions = subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE);

        int countGenerated = 0;
        LocalDate today = LocalDate.now();
        int targetMonth = today.getMonthValue();
        int targetYear = today.getYear();

        // Se quiser gerar para o mês seguinte, descomente:
        // targetMonth = today.plusMonths(1).getMonthValue();
        // targetYear = today.plusMonths(1).getYear();

        for (Subscription sub : activeSubscriptions) {

            boolean alreadyGenerated = installmentRepository.existsBySubscriptionIdAndMonthAndYear(
                    sub.getId(), targetMonth, targetYear
            );

            if (!alreadyGenerated) {
                LocalDate dueDate = LocalDate.of(targetYear, targetMonth, sub.getDueDay());

                Installment installment = Installment.builder()
                        .subscription(sub)
                        .amount(sub.getMonthlyFee())
                        .dueDate(dueDate)
                        .status(InstallmentStatus.PENDING)
                        .commissionPaid(false) 
                        .description("Mensalidade " + targetMonth + "/" + targetYear)
                        .build();

                installmentRepository.save(installment);
                countGenerated++;
            }
        }
        return countGenerated;
    }

    public List<InstallmentResponse> findAllSaasInstallments() {
        return installmentRepository.findBySubscriptionIdIsNotNullOrderByDueDateDesc().stream()
                .map(InstallmentMapper::toResponse) 
                .toList();
    }
}