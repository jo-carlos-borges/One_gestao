package br.com.one.mappers;

import br.com.one.domain.Product;
import br.com.one.records.ProductResponse;

public class ProductMapper {

    private ProductMapper() {}

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getBusinessUnit());
    }
}