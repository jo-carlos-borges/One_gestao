package br.com.one.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientRequest(
        @NotBlank @Size(min = 3, max = 100) String name,
        @Size(max = 18) String document,
        @NotBlank @Email @Size(max = 150) String email,
        @Size(max = 20) String phone,
        @Size(max = 100) String discordId) {
}
