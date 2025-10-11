package br.com.one.controller;

import br.com.one.records.InstallmentResponse;
import br.com.one.security.Roles;
import br.com.one.services.InstallmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/installments")
@RequiredArgsConstructor
@Secured({ Roles.ADMIN, Roles.USER })
public class InstallmentController {

    private final InstallmentService installmentService;

    @PatchMapping("/{id}/pay")
    public ResponseEntity<InstallmentResponse> markAsPaid(@PathVariable Long id) {
        InstallmentResponse updatedInstallment = installmentService.markAsPaid(id);
        return ResponseEntity.ok(updatedInstallment);
    }
}