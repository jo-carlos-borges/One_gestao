package br.com.one.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.one.domain.ProjectDeveloper;
import br.com.one.records.DeveloperScoreResponse;

public interface ProjectDeveloperRepository extends JpaRepository<ProjectDeveloper, Long> {
	
	@Query("""
	        SELECT new br.com.one.records.DeveloperScoreResponse(
	            pd.developer.id,
	            pd.developer.name,
	            SUM(pd.points)
	        )
	        FROM ProjectDeveloper pd
	        GROUP BY pd.developer.id, pd.developer.name
	        ORDER BY SUM(pd.points) DESC
	        """)
	List<DeveloperScoreResponse> findDeveloperScores();

}