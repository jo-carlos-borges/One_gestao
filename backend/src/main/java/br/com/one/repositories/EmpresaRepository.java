package br.com.one.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.one.domain.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}