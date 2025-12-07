package br.com.one.records;

import java.math.BigDecimal;

import br.com.one.domain.BusinessUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductRequest(
        @NotBlank
        @Size(min = 3, max = 150)
        String name,

        @NotNull(message = "O preço não pode ser nulo")
        @Positive(message = "O preço deve ser positivo")
        BigDecimal price,

        @Size(max = 500)
        String description,

        BusinessUnit businessUnit) {
}