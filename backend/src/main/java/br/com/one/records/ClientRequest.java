package br.com.one.records;

import java.util.Set;

import br.com.one.domain.BusinessUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ClientRequest(
		@NotBlank String name,
        @NotBlank String email,
        @NotBlank String phone,
        @NotBlank String document,
        @Size(max = 100) String discordId,
		@NotEmpty Set<BusinessUnit> businessUnits
		
) {}
