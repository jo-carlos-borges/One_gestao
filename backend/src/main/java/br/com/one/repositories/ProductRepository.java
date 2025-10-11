package br.com.one.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.one.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
