package br.com.one.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.one.domain.Empresa;
import br.com.one.security.Roles;
import br.com.one.services.EmpresaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/empresas")
@Secured(Roles.ADMIN)
public class EmpresaController {
	
	private final EmpresaService empresaService;
	
	@PostMapping
    public ResponseEntity<Empresa> criarEmpresa(@RequestBody Empresa empresa) {
        Empresa novaEmpresa = empresaService.criar(empresa);
        return new ResponseEntity<>(novaEmpresa, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Empresa>> buscarTodasEmpresas() {
        List<Empresa> empresas = empresaService.buscarTodas();
        return ResponseEntity.ok(empresas);
    }
}
