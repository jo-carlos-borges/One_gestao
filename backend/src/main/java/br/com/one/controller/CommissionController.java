package br.com.one.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.one.domain.User;
import br.com.one.records.DeveloperScoreResponse;
import br.com.one.records.SalespersonCommissionResponse;
import br.com.one.security.CustomUserDetails;
import br.com.one.security.Roles;
import br.com.one.services.CommissionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/commissions")
@RequiredArgsConstructor
@Secured({ Roles.ADMIN })
public class CommissionController {
	
	private final CommissionService commissionService;
	
	@GetMapping("/developers")
	public ResponseEntity<List<DeveloperScoreResponse>> getDeveloperScores() {
		return ResponseEntity.ok(commissionService.getDeveloperScores());
	}
	
	@GetMapping("/salespeople")
	@Secured({ Roles.ADMIN})
	public ResponseEntity<List<SalespersonCommissionResponse>> getSalespersonCommissions() {
		return ResponseEntity.ok(commissionService.getSalespersonCommissions());
	}
	
	@PostMapping("/salespeople/{id}/pay")
	@Secured({ Roles.ADMIN })
	public ResponseEntity<Void> paySalespersonCommission(
	        @PathVariable Long id, 
	        @AuthenticationPrincipal CustomUserDetails adminUserDetails) {

	    User adminUser = adminUserDetails.getUser();
	    commissionService.paySalespersonCommission(id, adminUser);
	    return ResponseEntity.ok().build();
	}
}
