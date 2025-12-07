package br.com.one.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.one.records.DeveloperScoreResponse;
import br.com.one.records.SalespersonCommissionResponse;
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
}
