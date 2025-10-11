package br.com.one.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.one.records.UserRequest;
import br.com.one.records.UserUpdateRequest;
import br.com.one.security.Roles;
import br.com.one.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Secured(Roles.ADMIN)
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
	
	private final UserService userService;
	
	@PostMapping
	public ResponseEntity<Object> save(@Valid @RequestBody UserRequest userRequest) {
		return userService.save(userRequest);
	}

	@GetMapping("{id}")
	public ResponseEntity<Object> getById(@PathVariable Long id) {
		return userService.getById(id);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		return userService.delete(id);
	}

	@PutMapping("{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
		return userService.update(id, userUpdateRequest);
	}
	
	@GetMapping
	public ResponseEntity<Object> get(Pageable pageable) {
		return userService.get(pageable);
	}
	
}