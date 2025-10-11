package br.com.one.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.one.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
	
    boolean existsByEmail(String email);
}