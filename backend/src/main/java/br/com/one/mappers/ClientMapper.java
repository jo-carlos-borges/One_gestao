package br.com.one.mappers;

import br.com.one.domain.Client;
import br.com.one.records.ClientResponse;

public class ClientMapper {
    private ClientMapper() {}

    public static ClientResponse toResponse(Client client) {
        return new ClientResponse(
            client.getId(),
            client.getName(),
            client.getDocument(),
            client.getEmail(),
            client.getPhone(),
            client.getDiscordId(),
            client.getCreationDate());
    }
}
