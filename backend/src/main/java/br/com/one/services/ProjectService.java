package br.com.one.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.one.domain.Client;
import br.com.one.domain.Project;
import br.com.one.domain.ProjectStatus;
import br.com.one.domain.User;
import br.com.one.records.ProjectRequest;
import br.com.one.records.ProjectResponse;
import br.com.one.repositories.ClientRepository;
import br.com.one.repositories.InstallmentRepository;
import br.com.one.repositories.ProjectDeveloperRepository;
import br.com.one.repositories.ProjectRepository;
import br.com.one.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
	
	private final ProjectRepository projectRepository;
	private final ProjectDeveloperRepository projectDeveloperRepository;
	private final InstallmentRepository installmentRepository;
	private final ClientRepository clientRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public ProjectResponse createProject(ProjectRequest request, User salesperson) {
		Client client = clientRepository.findById(request.clientId())
				.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
		
		Project project = Project.builder()
				.name(request.name())
				.description(request.description())
				.client(client)
				.salesperson(salesperson)
				.totalValue(request.totalValue())
				.status(ProjectStatus.PLANNED)
				.paymentMethod(request.paymentMethod())
				.build();
		
		Project savedProject = projectRepository.save(project);
		
		int totalPoints = request.totalValue().divide(new BigDecimal("1000"), 0, RoundingMode.FLOOR).intValue();
	}
}
