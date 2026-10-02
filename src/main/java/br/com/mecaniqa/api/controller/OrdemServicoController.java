package br.com.mecaniqa.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mecaniqa.api.dto.AtualizarStatusOrdemServicoRequest;
import br.com.mecaniqa.api.dto.CriarOrdemServicoRequest;
import br.com.mecaniqa.api.dto.OrdemServicoResponse;
import br.com.mecaniqa.api.service.OrdemDeServicoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController {

	private final OrdemDeServicoService service;

	public OrdemServicoController(OrdemDeServicoService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<OrdemServicoResponse> criar(@Valid @RequestBody CriarOrdemServicoRequest request) {
		OrdemServicoResponse criada = service.criar(request);
		return ResponseEntity.created(URI.create("/api/ordens-servico/" + criada.getCodigo()))
				.body(criada);
	}

	@GetMapping
	public ResponseEntity<List<OrdemServicoResponse>> listar() {
		return ResponseEntity.ok(service.listar());
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<OrdemServicoResponse> buscar(@PathVariable Long codigo) {
		return ResponseEntity.ok(service.buscar(codigo));
	}

	@PatchMapping("/{codigo}/status")
	public ResponseEntity<OrdemServicoResponse> atualizarStatus(@PathVariable Long codigo,
			@Valid @RequestBody AtualizarStatusOrdemServicoRequest request) {
		return ResponseEntity.ok(service.atualizarStatus(codigo, request));
	}
}
