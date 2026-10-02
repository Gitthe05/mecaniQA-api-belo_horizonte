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

import br.com.mecaniqa.api.dto.AtualizarServicoRequest;
import br.com.mecaniqa.api.dto.CriarServicoRequest;
import br.com.mecaniqa.api.dto.ServicoResponse;
import br.com.mecaniqa.api.service.ServicoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

	private final ServicoService service;

	public ServicoController(ServicoService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<ServicoResponse> criar(@Valid @RequestBody CriarServicoRequest request) {
		ServicoResponse servicoCriado = service.criar(request);
		URI localizacao = URI.create("/api/servicos/" + servicoCriado.getCodigo());
		return ResponseEntity.created(localizacao).body(servicoCriado);
	}

	@GetMapping
	public ResponseEntity<List<ServicoResponse>> listar() {
		return ResponseEntity.ok(service.listar());
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<ServicoResponse> buscar(@PathVariable Long codigo) {
		return ResponseEntity.ok(service.buscar(codigo));
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<ServicoResponse> atualizar(
			@PathVariable Long codigo,
			@Valid @RequestBody AtualizarServicoRequest request) {
		return ResponseEntity.ok(service.atualizar(codigo, request));
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> excluir(@PathVariable Long codigo) {
		service.excluir(codigo);
		return ResponseEntity.noContent().build();
	}
}
