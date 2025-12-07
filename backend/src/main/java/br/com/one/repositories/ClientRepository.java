package br.com.one.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.one.domain.BusinessUnit;
import br.com.one.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
	
    boolean existsByEmail(String email);
    
    @Query("SELECT c FROM Client c JOIN c.businessUnits bu WHERE bu = :unit")
    Page<Client> findByBusinessUnit(BusinessUnit unit, Pageable pageable);
}