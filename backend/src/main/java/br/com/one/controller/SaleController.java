package br.com.one.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.one.domain.User;
import br.com.one.records.SaleRequest;
import br.com.one.records.SaleResponse;
import br.com.one.records.SaleSummaryResponse;
import br.com.one.security.CustomUserDetails;
import br.com.one.security.Roles;
import br.com.one.services.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/gta-sales")
@RequiredArgsConstructor
@Secured({ Roles.ADMIN, Roles.USER })
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<SaleResponse> createSale(@Valid @RequestBody SaleRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalStateException("UserDetails is null - authentication failed");
        }
        User salesperson = userDetails.getUser();
        SaleResponse newSaleResponse = saleService.createSale(request, salesperson);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSaleResponse);
    }

    @GetMapping
    public ResponseEntity<Page<SaleSummaryResponse>> getAllSales(Pageable pageable) {
        Page<SaleSummaryResponse> sales = saleService.findAll(pageable);
        return ResponseEntity.ok(sales);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> getSaleById(@PathVariable Long id) {
    	return saleService.findSaleDetailsById(id)
    			.map(ResponseEntity::ok)
    			.orElse(ResponseEntity.notFound().build());
    }
}