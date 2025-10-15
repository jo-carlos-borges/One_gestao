package br.com.one.repositories;

import br.com.one.domain.Project;
import br.com.one.records.ProjectSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import br.com.one.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	
	@Query("""
	        SELECT new br.com.one.records.ProjectSummaryResponse(
	            p.id,
	            p.name,
	            p.client.name,
	            p.totalValue,
	            p.status,
	            p.creationDate,
	            CAST(COUNT(pd) AS int)
	        )
	        FROM Project p
	        LEFT JOIN p.projectDevelopers pd
	        GROUP BY p.id, p.name, p.client.name, p.totalValue, p.status, p.creationDate
	        """)
    Page<ProjectSummaryResponse> findProjectSummaries(Pageable pageable);
}