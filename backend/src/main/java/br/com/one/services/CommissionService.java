package br.com.one.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.one.domain.CommissionPayout;
import br.com.one.domain.Installment;
import br.com.one.domain.User;
import br.com.one.records.DeveloperScoreResponse;
import br.com.one.records.SalespersonCommissionResponse;
import br.com.one.repositories.CommissionPayoutRepository;
import br.com.one.repositories.InstallmentRepository;
import br.com.one.repositories.ProjectDeveloperRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommissionService {
	
	private final ProjectDeveloperRepository projectDeveloperRepository;
	private final InstallmentRepository installmentRepository;
	private final CommissionPayoutRepository commissionPayoutRepository;
	
	public List<DeveloperScoreResponse> getDeveloperScores() {
		return projectDeveloperRepository.findDeveloperScores();
	}
	
	public List<SalespersonCommissionResponse> getSalespersonCommissions() {
		return installmentRepository.findPayableSalespersonCommissions();
	}
	
	@Transactional
	public void paySalespersonCommission(Long salespersonId, User adminUser) {
		List<Installment> installmentsToPay = installmentRepository.findUnpaidCommissionsBySalespersonId(salespersonId);
		
		if (installmentsToPay.isEmpty()) {
			throw new IllegalStateException("Nenhuma comissão pendente encontrada para este usuario");
		}
		
		BigDecimal totalAmount = BigDecimal.ZERO;
		
		for (Installment installment : installmentsToPay) {
			BigDecimal commissionAmount = installment.getAmount().multiply(new BigDecimal("0.10"));
			totalAmount = totalAmount.add(commissionAmount);
			
			installment.setCommissionPaid(true);
		}
		
		installmentRepository.saveAll(installmentsToPay);
		
		CommissionPayout payout = CommissionPayout.builder()
				.salesperon(installmentsToPay.get(0).getSale() != null ? 
                        installmentsToPay.get(0).getSale().getSalesPerson() : 
                        installmentsToPay.get(0).getProject().getSalesperson())
				.amountPaid(totalAmount)
				.payoutDate(LocalDateTime.now())
				.processedBy(adminUser)
				.build();
		
		commissionPayoutRepository.save(payout);
	}
}
