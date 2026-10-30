package br.com.mecaniqa.api.controller;

import br.com.mecaniqa.api.dto.AtualizarStatusPedidoRequest;
import br.com.mecaniqa.api.dto.CriarPedidoPecasRequest;
import br.com.mecaniqa.api.dto.ItemPedidoRequest;
import br.com.mecaniqa.api.dto.PedidoPecasResponse;
import br.com.mecaniqa.api.service.PedidoPecasService;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos-pecas")
public class PedidoPecasController {

  private final PedidoPecasService service;

  public PedidoPecasController(PedidoPecasService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<PedidoPecasResponse> criar(@RequestBody CriarPedidoPecasRequest request) {
    return ControllerSupport.executar(
        () -> {
          ControllerSupport.validar(request);
          PedidoPecasResponse criado = service.criar(request);
          return ResponseEntity.created(URI.create("/api/pedidos-pecas/" + criado.getCodigo()))
              .body(criado);
        },
        "/api/pedidos-pecas");
  }

  @GetMapping
  public ResponseEntity<List<PedidoPecasResponse>> listar() {
    return ControllerSupport.executar(
        () -> ResponseEntity.ok(service.listar()), "/api/pedidos-pecas");
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<PedidoPecasResponse> buscar(@PathVariable Long codigo) {
    return ControllerSupport.executar(
        () -> ResponseEntity.ok(service.buscar(codigo)), "/api/pedidos-pecas/" + codigo);
  }

  @PostMapping("/{codigo}/itens")
  public ResponseEntity<PedidoPecasResponse> adicionarItem(
      @PathVariable Long codigo, @RequestBody ItemPedidoRequest request) {
    return ControllerSupport.executar(
        () -> {
          ControllerSupport.validar(request);
          return ResponseEntity.ok(service.adicionarItem(codigo, request));
        },
        "/api/pedidos-pecas/" + codigo + "/itens");
  }

  @PatchMapping("/{codigo}/status")
  public ResponseEntity<PedidoPecasResponse> atualizarStatus(
      @PathVariable Long codigo, @RequestBody AtualizarStatusPedidoRequest request) {
    return ControllerSupport.executar(
        () -> {
          ControllerSupport.validar(request);
          return ResponseEntity.ok(service.atualizarStatus(codigo, request));
        },
        "/api/pedidos-pecas/" + codigo + "/status");
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<Void> excluir(@PathVariable Long codigo) {
    return ControllerSupport.executar(
        () -> {
          service.excluir(codigo);
          return ResponseEntity.noContent().build();
        },
        "/api/pedidos-pecas/" + codigo);
  }
}
