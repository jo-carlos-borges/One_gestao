package br.com.one.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.one.domain.Sale;
import br.com.one.repositories.SaleRepository;
import br.com.one.security.Roles;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/bonuses")
@RequiredArgsConstructor
@Secured({ Roles.ADMIN })
public class BonusController {

    private final SaleRepository saleRepository;

    @GetMapping("/installations/pending")
    public ResponseEntity<List<Map<String, Object>>> getPendingInstallationBonuses() {
        List<Sale> sales = saleRepository.findByOutOfHoursTrueAndInstallationBonusPaidFalse();
        
        List<Map<String, Object>> response = sales.stream().map(s -> Map.<String, Object>of(
            "saleId", s.getId(),
            "installerName", s.getInstaller() != null ? s.getInstaller().getName() : "N/A",
            "saleDate", s.getSaleDate(),
            "amount", s.getInstallationBonus()
        )).toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/installations/{saleId}/pay")
    @Transactional
    public ResponseEntity<Void> payInstallationBonus(@PathVariable Long saleId) {
        Sale sale = saleRepository.findById(saleId)
            .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        if (!sale.isOutOfHours()) {
             throw new RuntimeException("Esta venda não possui bônus de horário.");
        }

        sale.setInstallationBonusPaid(true);
        saleRepository.save(sale);
        return ResponseEntity.ok().build();
    }
}