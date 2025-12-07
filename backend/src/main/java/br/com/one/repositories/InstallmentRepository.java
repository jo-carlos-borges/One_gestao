package br.com.one.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.one.domain.Installment;
import br.com.one.records.SalespersonCommissionResponse;

public interface InstallmentRepository extends JpaRepository<Installment, Long> {
	
	List<Installment> findBySaleId(Long saleId);
	List<Installment> findByProjectId(Long projectId);
	
	@Query("""
		    SELECT new br.com.one.records.SalespersonCommissionResponse(
		        CAST(COALESCE(s_sp.id, p_sp.id) AS Long),
		        CAST(COALESCE(s_sp.name, p_sp.name) AS String),
		        CAST(SUM(i.amount * 0.10) AS BigDecimal)
		    )
		    FROM Installment i
		    LEFT JOIN i.sale s
		    LEFT JOIN i.project p
		    LEFT JOIN s.salesPerson s_sp
		    LEFT JOIN p.salesperson p_sp
		    WHERE i.status = 'PAID' 
		      AND i.commissionPaid = false
		      AND (s_sp.id IS NOT NULL OR p_sp.id IS NOT NULL)
		    GROUP BY COALESCE(s_sp.id, p_sp.id), COALESCE(s_sp.name, p_sp.name)
		    HAVING SUM(i.amount) > 0
		    """)
	List<SalespersonCommissionResponse> findPayableSalespersonCommissions();
	
	@Query("""
		    SELECT i FROM Installment i
		    LEFT JOIN i.sale s
		    LEFT JOIN i.project p
		    WHERE (s.salesPerson.id = :salespersonId OR p.salesperson.id = :salespersonId)
		    AND i.status = 'PAID'
		    AND i.commissionPaid = false
		    """)
	List<Installment> findUnpaidCommissionsBySalespersonId(Long salespersonId);
	
	List<Installment> findBySubscriptionId(Long subscriptionId);
	
}