package br.com.one.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import br.com.one.domain.Client;
import br.com.one.mappers.ClientMapper;
import br.com.one.records.ClientRequest;
import br.com.one.records.ClientResponse;
import br.com.one.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ResponseEntity<Object> save(ClientRequest request) {
        if (clientRepository.existsByEmail(request.email())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email já em uso.");
        }
        Client client = Client.builder()
            .name(request.name())
            .document(request.document())
            .email(request.email())
            .phone(request.phone())
            .discordId(request.discordId())
            .build();
        
        Client savedClient = clientRepository.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toResponse(savedClient));
    }

    public ResponseEntity<Object> findById(Long id) {
        return clientRepository.findById(id)
            .<ResponseEntity<Object>>map(client -> ResponseEntity.ok(ClientMapper.toResponse(client)))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado"));
    }

    public ResponseEntity<Page<ClientResponse>> findAll(Pageable pageable) {
        Page<ClientResponse> clients = clientRepository.findAll(pageable).map(ClientMapper::toResponse);
        return ResponseEntity.ok(clients);
    }

    public ResponseEntity<Object> update(Long id, ClientRequest request) {
        return clientRepository.findById(id)
            .<ResponseEntity<Object>>map(client -> {
                client.setName(request.name());
                client.setDocument(request.document());
                client.setEmail(request.email());
                client.setPhone(request.phone());
                client.setDiscordId(request.discordId());
                Client updatedClient = clientRepository.save(client);
                return ResponseEntity.ok(ClientMapper.toResponse(updatedClient));
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado"));
    }

    public ResponseEntity<Object> delete(Long id) {
        if (!clientRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }
        clientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}