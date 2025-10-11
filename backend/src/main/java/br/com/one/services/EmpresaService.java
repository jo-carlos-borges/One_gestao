package br.com.one.services;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.one.domain.Empresa;
import br.com.one.repositories.EmpresaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpresaService {
	
	private final EmpresaRepository empresaRepository;
	
	public Empresa criar(Empresa empresa) {
		return empresaRepository.save(empresa);
	}
	
	public List<Empresa> buscarTodas() {
		return empresaRepository.findAll();
	}
}
