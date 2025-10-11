package br.com.one.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.one.domain.Product;
import br.com.one.mappers.ProductMapper;
import br.com.one.records.ProductRequest;
import br.com.one.records.ProductResponse;
import br.com.one.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ResponseEntity<Object> save(ProductRequest request) {
        Product product = Product.builder()
            .name(request.name())
            .price(request.price())
            .description(request.description())
            .build();
        
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductMapper.toResponse(savedProduct));
    }

    public ResponseEntity<Object> findById(Long id) {
        return productRepository.findById(id)
            .<ResponseEntity<Object>>map(product -> ResponseEntity.ok(ProductMapper.toResponse(product)))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado."));
    }

    public ResponseEntity<Page<ProductResponse>> findAll(Pageable pageable) {
        Page<ProductResponse> products = productRepository.findAll(pageable).map(ProductMapper::toResponse);
        return ResponseEntity.ok(products);
    }

    public ResponseEntity<Object> update(Long id, ProductRequest request) {
        return productRepository.findById(id)
            .<ResponseEntity<Object>>map(product -> {
                product.setName(request.name());
                product.setPrice(request.price());
                product.setDescription(request.description());
                Product updatedProduct = productRepository.save(product);
                return ResponseEntity.ok(ProductMapper.toResponse(updatedProduct));
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado."));
    }

    public ResponseEntity<Object> delete(Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}