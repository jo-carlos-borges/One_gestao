package br.com.one.services;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.one.records.DeveloperScoreResponse;
import br.com.one.records.SalespersonCommissionResponse;
import br.com.one.repositories.InstallmentRepository;
import br.com.one.repositories.ProjectDeveloperRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommissionService {
	
	private final ProjectDeveloperRepository projectDeveloperRepository;
	private final InstallmentRepository installmentRepository;
	
	public List<DeveloperScoreResponse> getDeveloperScores() {
		return projectDeveloperRepository.findDeveloperScores();
	}
	
	public List<SalespersonCommissionResponse> getSalespersonCommissions() {
		return installmentRepository.findPayableSalespersonCommissions();
	}
}
