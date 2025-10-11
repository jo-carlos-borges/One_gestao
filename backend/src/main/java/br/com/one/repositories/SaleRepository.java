package br.com.one.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.one.domain.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
