package br.com.one.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.one.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}