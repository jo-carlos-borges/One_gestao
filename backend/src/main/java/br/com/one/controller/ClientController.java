package br.com.one.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.one.domain.BusinessUnit;
import br.com.one.records.ClientRequest;
import br.com.one.records.ClientResponse;
import br.com.one.security.Roles;
import br.com.one.services.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Secured({ Roles.ADMIN, Roles.USER })
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody ClientRequest request) {
        return clientService.save(request);
    }

    @GetMapping
    public ResponseEntity<Page<ClientResponse>> getAllClients(
            @PageableDefault(size = 10, sort = "name") Pageable pageable,
            @RequestParam(required = false) BusinessUnit unit) {

        return ResponseEntity.ok(clientService.findAll(pageable, unit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody ClientRequest request) {
        return clientService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return clientService.delete(id);
    }
}