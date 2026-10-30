package br.com.mecaniqa.api.controller;

import br.com.mecaniqa.api.dto.AtualizarStatusOrdemServicoRequest;
import br.com.mecaniqa.api.dto.CriarOrdemServicoRequest;
import br.com.mecaniqa.api.dto.OrdemServicoResponse;
import br.com.mecaniqa.api.service.OrdemDeServicoService;
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
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController {

  private final OrdemDeServicoService service;

  public OrdemServicoController(OrdemDeServicoService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<OrdemServicoResponse> criar(@RequestBody CriarOrdemServicoRequest request) {
    return ControllerSupport.executar(
        () -> {
          ControllerSupport.validar(request);
          OrdemServicoResponse criada = service.criar(request);
          return ResponseEntity.created(URI.create("/api/ordens-servico/" + criada.getCodigo()))
              .body(criada);
        },
        "/api/ordens-servico");
  }

  @GetMapping
  public ResponseEntity<List<OrdemServicoResponse>> listar() {
    return ControllerSupport.executar(
        () -> ResponseEntity.ok(service.listar()), "/api/ordens-servico");
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<OrdemServicoResponse> buscar(@PathVariable Long codigo) {
    return ControllerSupport.executar(
        () -> ResponseEntity.ok(service.buscar(codigo)), "/api/ordens-servico/" + codigo);
  }

  @PatchMapping("/{codigo}/status")
  public ResponseEntity<OrdemServicoResponse> atualizarStatus(
      @PathVariable Long codigo, @RequestBody AtualizarStatusOrdemServicoRequest request) {
    return ControllerSupport.executar(
        () -> {
          ControllerSupport.validar(request);
          return ResponseEntity.ok(service.atualizarStatus(codigo, request));
        },
        "/api/ordens-servico/" + codigo);
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<Void> excluir(@PathVariable Long codigo) {
    return ControllerSupport.executar(
        () -> {
          service.excluir(codigo);
          return ResponseEntity.noContent().build();
        },
        "/api/ordens-servico/" + codigo);
  }
}
