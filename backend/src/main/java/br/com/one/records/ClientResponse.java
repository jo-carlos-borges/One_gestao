package br.com.one.records;

import java.time.LocalDateTime;

public record ClientResponse(
        Long id,
        String name,
        String document,
        String email,
        String phone,
        String discordId,
        LocalDateTime creationDate) {
}
