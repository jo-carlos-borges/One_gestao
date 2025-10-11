package br.com.one.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.one.domain.Installment;

public interface InstallmentRepository extends JpaRepository<Installment, Long> {
	
	List<Installment> findBySaleId(Long saleId);
	List<Installment> findByProjectId(Long projectId);
	
}