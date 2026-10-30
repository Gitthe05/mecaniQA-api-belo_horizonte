package br.com.mecaniqa.api.controller;

import br.com.mecaniqa.api.dto.AtualizarServicoRequest;
import br.com.mecaniqa.api.dto.CriarServicoRequest;
import br.com.mecaniqa.api.dto.ServicoResponse;
import br.com.mecaniqa.api.service.ServicoService;
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

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

  private final ServicoService service;

  public ServicoController(ServicoService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<ServicoResponse> criar(@RequestBody CriarServicoRequest request) {
    return ControllerSupport.executar(
        () -> {
          ControllerSupport.validar(request);
          ServicoResponse criado = service.criar(request);
          return ResponseEntity.created(URI.create("/api/servicos/" + criado.getCodigo()))
              .body(criado);
        },
        "/api/servicos");
  }

  @GetMapping
  public ResponseEntity<List<ServicoResponse>> listar() {
    return ControllerSupport.executar(() -> ResponseEntity.ok(service.listar()), "/api/servicos");
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<ServicoResponse> buscar(@PathVariable Long codigo) {
    return ControllerSupport.executar(
        () -> ResponseEntity.ok(service.buscar(codigo)), "/api/servicos/" + codigo);
  }

  @PutMapping("/{codigo}")
  public ResponseEntity<ServicoResponse> atualizar(
      @PathVariable Long codigo, @RequestBody AtualizarServicoRequest request) {
    return ControllerSupport.executar(
        () -> {
          ControllerSupport.validar(request);
          return ResponseEntity.ok(service.atualizar(codigo, request));
        },
        "/api/servicos/" + codigo);
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<Void> excluir(@PathVariable Long codigo) {
    return ControllerSupport.executar(
        () -> {
          service.excluir(codigo);
          return ResponseEntity.noContent().build();
        },
        "/api/servicos/" + codigo);
  }
}
