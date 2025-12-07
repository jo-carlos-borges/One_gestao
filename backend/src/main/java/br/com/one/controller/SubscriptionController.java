package br.com.one.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.one.domain.SubscriptionStatus;
import br.com.one.domain.User;
import br.com.one.records.InstallmentResponse;
import br.com.one.records.SubscriptionRequest;
import br.com.one.records.SubscriptionResponse;
import br.com.one.security.CustomUserDetails;
import br.com.one.security.Roles;
import br.com.one.services.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
@Secured({ Roles.ADMIN, Roles.USER })
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @Valid @RequestBody SubscriptionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalStateException("UserDetails is null - authentication failed");
        }
        User salesperson = userDetails.getUser();
        SubscriptionResponse response = subscriptionService.createSubscription(request, salesperson);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions(
            @RequestParam(required = false) SubscriptionStatus status) {
        
        return ResponseEntity.ok(subscriptionService.findAll(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.findById(id));
    }
    
    @PatchMapping("/{id}/activate")
    @Secured({ Roles.ADMIN })
    public ResponseEntity<Void> activateSubscription(@PathVariable Long id) {
        subscriptionService.activateSubscription(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/generate-invoices")
    @Secured({ Roles.ADMIN })
    public ResponseEntity<String> generateInvoices() {
        int count = subscriptionService.generateMonthlyInvoicesForActiveClients();
        return ResponseEntity.ok("Faturas geradas: " + count);
    }

    @GetMapping("/financial")
    public ResponseEntity<List<InstallmentResponse>> getSaasFinancial() {
        return ResponseEntity.ok(subscriptionService.findAllSaasInstallments());
    }
}

