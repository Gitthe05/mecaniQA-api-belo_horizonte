package br.com.mecaniqa.api.controller;

import br.com.mecaniqa.api.dto.AtualizarPecaRequest;
import br.com.mecaniqa.api.dto.CriarPecaRequest;
import br.com.mecaniqa.api.dto.PecaResponse;
import br.com.mecaniqa.api.exception.ApiError;
import br.com.mecaniqa.api.exception.ErroDePersistenciaException;
import br.com.mecaniqa.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniqa.api.service.PecaService;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/pecas")
public class PecaController {

  private final PecaService service;

  public PecaController(PecaService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<PecaResponse> criar(@RequestBody CriarPecaRequest request) {
    return executar(
        () -> {
          ControllerSupport.validar(request);
          PecaResponse pecaCriada = service.criar(request);
          return ResponseEntity.created(URI.create("/api/pecas/" + pecaCriada.getCodigo()))
              .body(pecaCriada);
        },
        "/api/pecas");
  }

  @GetMapping
  public ResponseEntity<List<PecaResponse>> listar() {
    return executar(() -> ResponseEntity.ok(service.listar()), "/api/pecas");
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<PecaResponse> buscar(@PathVariable Long codigo) {
    return executar(() -> ResponseEntity.ok(service.buscar(codigo)), "/api/pecas/" + codigo);
  }

  @PutMapping("/{codigo}")
  public ResponseEntity<PecaResponse> atualizar(
      @PathVariable Long codigo, @RequestBody AtualizarPecaRequest request) {
    return executar(
        () -> {
          ControllerSupport.validar(request);
          return ResponseEntity.ok(service.atualizar(codigo, request));
        },
        "/api/pecas/" + codigo);
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<Void> excluir(@PathVariable Long codigo) {
    return executar(
        () -> {
          service.excluir(codigo);
          return ResponseEntity.noContent().build();
        },
        "/api/pecas/" + codigo);
  }

  @SuppressWarnings("unchecked")
  private <T> ResponseEntity<T> executar(Supplier<ResponseEntity<T>> operacao, String caminho) {
    try {
      return operacao.get();
    } catch (br.com.mecaniqa.api.exception.DadosInvalidosException exception) {
      ApiError erro =
          new ApiError(
              LocalDateTime.now(),
              400,
              "Bad Request",
              exception.getMessage(),
              caminho,
              exception.getCampos());
      return ResponseEntity.badRequest().body((T) erro);
    } catch (RecursoNaoEncontradoException exception) {
      ApiError erro =
          new ApiError(
              LocalDateTime.now(), 404, "Not Found", exception.getMessage(), caminho, Map.of());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body((T) erro);
    } catch (ErroDePersistenciaException exception) {
      ApiError erro =
          new ApiError(
              LocalDateTime.now(),
              500,
              "Internal Server Error",
              "Falha ao acessar o banco de dados",
              caminho,
              Map.of());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((T) erro);
    }
  }
}
