package br.com.one.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.one.domain.ProjectDeveloper;

public interface ProjectDeveloperRepository extends JpaRepository<ProjectDeveloper, Long> {

}