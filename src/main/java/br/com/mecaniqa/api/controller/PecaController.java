package br.com.mecaniqa.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mecaniqa.api.dto.AtualizarPecaRequest;
import br.com.mecaniqa.api.dto.CriarPecaRequest;
import br.com.mecaniqa.api.dto.PecaResponse;
import br.com.mecaniqa.api.service.PecaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {

	private final PecaService service;

	public PecaController(PecaService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<PecaResponse> criar(@Valid @RequestBody CriarPecaRequest request) {
		PecaResponse pecaCriada = service.criar(request);
		URI localizacao = URI.create("/api/pecas/" + pecaCriada.getCodigo());
		return ResponseEntity.created(localizacao).body(pecaCriada);
	}

	@GetMapping
	public ResponseEntity<List<PecaResponse>> listar() {
		return ResponseEntity.ok(service.listar());
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<PecaResponse> buscar(@PathVariable Long codigo) {
		return ResponseEntity.ok(service.buscar(codigo));
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<PecaResponse> atualizar(
			@PathVariable Long codigo,
			@Valid @RequestBody AtualizarPecaRequest request) {
		return ResponseEntity.ok(service.atualizar(codigo, request));
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> excluir(@PathVariable Long codigo) {
		service.excluir(codigo);
		return ResponseEntity.noContent().build();
	}
}
