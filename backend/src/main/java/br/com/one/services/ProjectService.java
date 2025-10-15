package br.com.one.services;

import br.com.one.domain.*;
import br.com.one.mappers.ProjectMapper;
import br.com.one.records.ProjectRequest;
import br.com.one.records.ProjectResponse;
import br.com.one.records.ProjectSummaryResponse;
import br.com.one.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
	
	private final ProjectRepository projectRepository;
	private final ProjectDeveloperRepository projectDeveloperRepository;
	private final InstallmentRepository installmentRepository;
	private final ClientRepository clientRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public ProjectResponse createProject (ProjectRequest request, User salesperson) {
		Client client = clientRepository.findById(request.clientId())
				 .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
		
		List<User> developers = userRepository.findAllById(request.developerIds());
		if (developers.size() != request.developerIds().size()) {
			throw new EntityNotFoundException("Um ou mais desenvolvedores não foram encontrados.");
        }
		
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
		Set<ProjectDeveloper> projectDeveloper = new HashSet<>();
		int devCount = developers.size();
		
		if (devCount > 0) {
			if (totalPoints == 1 && devCount > 1) {
				for (User dev : developers) {
					projectDeveloper.add(ProjectDeveloper.builder().project(savedProject).developer(dev).points(1).build());
				}
			} else {
				int pointsPerDev = totalPoints / devCount;
				int remainingPoints = totalPoints % devCount;
				
				for (User dev : developers) {
					int points = pointsPerDev;
					if (remainingPoints > 0) {
						points++;
						remainingPoints--;
					}
					projectDeveloper.add(ProjectDeveloper.builder().project(savedProject).developer(dev).points(points).build());
				}
			}
			projectDeveloperRepository.saveAll(projectDeveloper);
		}
		savedProject.setProjectDevelopers(projectDeveloper);
		
		List<Installment> installments = new ArrayList<>();
		
		if (request.paymentMethod() == PaymentMethod.INSTALLMENTS_PLAN && request.numberOfInstallments() != null && request.numberOfInstallments() > 0) {
			BigDecimal installmentValue = request.totalValue().divide(BigDecimal.valueOf(request.numberOfInstallments()), 2, RoundingMode.HALF_UP);
			
			for (int i = 1; i <= request.numberOfInstallments(); i++) {
				installments.add(Installment.builder()
						.project(savedProject)
						.amount(installmentValue)
						.dueDate(LocalDate.now().plusMonths(i))
						.status(InstallmentStatus.PENDING)
						.build());
			}
			installmentRepository.saveAll(installments);
		}
		return ProjectMapper.toResponse(savedProject, installments);
	}
	
	public Page<ProjectSummaryResponse> findAll(Pageable pageable) {
	    return projectRepository.findProjectSummaries(pageable);
	}
}
