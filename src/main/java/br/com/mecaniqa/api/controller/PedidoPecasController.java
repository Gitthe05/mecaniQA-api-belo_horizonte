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

import br.com.mecaniqa.api.dto.AtualizarStatusPedidoRequest;
import br.com.mecaniqa.api.dto.CriarPedidoPecasRequest;
import br.com.mecaniqa.api.dto.ItemPedidoRequest;
import br.com.mecaniqa.api.dto.PedidoPecasResponse;
import br.com.mecaniqa.api.service.PedidoPecasService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos-pecas")
public class PedidoPecasController {

	private final PedidoPecasService service;

	public PedidoPecasController(PedidoPecasService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<PedidoPecasResponse> criar(@Valid @RequestBody CriarPedidoPecasRequest request) {
		PedidoPecasResponse criado = service.criar(request);
		return ResponseEntity.created(URI.create("/api/pedidos-pecas/" + criado.getCodigo()))
				.body(criado);
	}

	@GetMapping
	public ResponseEntity<List<PedidoPecasResponse>> listar() {
		return ResponseEntity.ok(service.listar());
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<PedidoPecasResponse> buscar(@PathVariable Long codigo) {
		return ResponseEntity.ok(service.buscar(codigo));
	}

	@PostMapping("/{codigo}/itens")
	public ResponseEntity<PedidoPecasResponse> adicionarItem(@PathVariable Long codigo,
			@Valid @RequestBody ItemPedidoRequest request) {
		return ResponseEntity.ok(service.adicionarItem(codigo, request));
	}

	@PatchMapping("/{codigo}/status")
	public ResponseEntity<PedidoPecasResponse> atualizarStatus(@PathVariable Long codigo,
			@Valid @RequestBody AtualizarStatusPedidoRequest request) {
		return ResponseEntity.ok(service.atualizarStatus(codigo, request));
	}
}
