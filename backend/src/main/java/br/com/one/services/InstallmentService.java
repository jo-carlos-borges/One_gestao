package br.com.one.services;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.one.domain.Installment;
import br.com.one.domain.InstallmentStatus;
import br.com.one.records.InstallmentResponse;
import br.com.one.repositories.InstallmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstallmentService {
	
	private final InstallmentRepository installmentRepository;
	
	@Transactional
	public InstallmentResponse markAsPaid(Long installmentId) {
		Installment installment = installmentRepository.findById(installmentId)
				.orElseThrow(() -> new EntityNotFoundException("Parcela não encontrata: " + installmentId));
		
		if (installment.getStatus() == InstallmentStatus.PAID) {
			throw new IllegalStateException("Esta parcela já foi paga");
		}
		
		installment.setStatus(InstallmentStatus.PAID);
		installment.setPaymentDate(LocalDate.now());
		
		Installment savedInstallment = installmentRepository.save(installment);
		
		return new InstallmentResponse(
				savedInstallment.getId(),
				savedInstallment.getAmount(),
				savedInstallment.getDueDate(),
				savedInstallment.getPaymentDate(),
				savedInstallment.getStatus()
		);
	}
}
