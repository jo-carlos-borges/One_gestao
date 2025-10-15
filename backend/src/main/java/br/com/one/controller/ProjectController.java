package br.com.one.controller;

import br.com.one.domain.User;
import br.com.one.records.ProjectRequest;
import br.com.one.records.ProjectResponse;
import br.com.one.records.ProjectSummaryResponse;
import br.com.one.security.CustomUserDetails;
import br.com.one.security.Roles;
import br.com.one.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Secured({ Roles.ADMIN, Roles.USER })
public class ProjectController {
	
	private final ProjectService projectService;
	
	@PostMapping
	public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User salesperson = userDetails.getUser();
        ProjectResponse newProject = projectService.createProject(request, salesperson);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProject);
	}
	
	@GetMapping
	public ResponseEntity<Page<ProjectSummaryResponse>> getAllProjects(Pageable pageable) {
        return ResponseEntity.ok(projectService.findAll(pageable));
    }
}
