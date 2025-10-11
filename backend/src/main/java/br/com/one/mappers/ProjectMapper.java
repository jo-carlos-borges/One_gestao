package br.com.one.mappers;

import br.com.one.domain.Installment;
import br.com.one.domain.Project;
import br.com.one.records.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectMapper {
    private ProjectMapper() {}

    public static ProjectResponse toResponse(Project project, List<Installment> installments) {
        return new ProjectResponse(
            project.getId(),
            project.getName(),
            project.getDescription(),
            ClientMapper.toResponse(project.getClient()),
            new UserSummaryResponse(project.getSalesperson().getId(), project.getSalesperson().getName()),
            project.getProjectDevelopers().stream()
                .map(pd -> new ProjectDeveloperResponse(pd.getDeveloper().getId(), pd.getDeveloper().getName(), pd.getPoints()))
                .collect(Collectors.toSet()),
            project.getTotalValue(),
            project.getStatus(),
            project.getPaymentMethod(),
            project.getCreationDate(),
            installments != null ? installments.stream()
                .map(i -> new InstallmentResponse(i.getId(), i.getAmount(), i.getDueDate(), i.getPaymentDate(), i.getStatus()))
                .collect(Collectors.toList()) : Collections.emptyList()
        );
    }

    public static ProjectSummaryResponse toSummaryResponse(Project project) {
        return new ProjectSummaryResponse(
            project.getId(),
            project.getName(),
            project.getClient().getName(),
            project.getTotalValue(),
            project.getStatus(),
            project.getCreationDate(),
            project.getProjectDevelopers() != null ? project.getProjectDevelopers().size() : 0
        );
    }
}