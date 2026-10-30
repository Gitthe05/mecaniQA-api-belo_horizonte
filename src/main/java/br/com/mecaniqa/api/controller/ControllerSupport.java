package br.com.mecaniqa.api.controller;

import br.com.mecaniqa.api.exception.ApiError;
import br.com.mecaniqa.api.exception.DadosInvalidosException;
import br.com.mecaniqa.api.exception.ErroDePersistenciaException;
import br.com.mecaniqa.api.exception.RecursoNaoEncontradoException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** Tratamento explícito chamado pelos controllers, sem mecanismo global do Spring. */
final class ControllerSupport {
  private static final Validator VALIDATOR =
      Validation.buildDefaultValidatorFactory().getValidator();

  private ControllerSupport() {}

  static void validar(Object request) {
    if (request == null) {
      throw new DadosInvalidosException(Map.of("body", "Corpo da requisição é obrigatório"));
    }
    Set<ConstraintViolation<Object>> violations = VALIDATOR.validate(request);
    if (violations.isEmpty()) return;

    Map<String, String> campos = new LinkedHashMap<>();
    for (ConstraintViolation<Object> violation : violations) {
      campos.put(violation.getPropertyPath().toString(), violation.getMessage());
    }
    throw new DadosInvalidosException(campos);
  }

  static <T> ResponseEntity<T> executar(Supplier<ResponseEntity<T>> acao, String caminho) {
    try {
      return acao.get();
    } catch (DadosInvalidosException e) {
      ApiError body =
          new ApiError(
              LocalDateTime.now(),
              HttpStatus.BAD_REQUEST.value(),
              HttpStatus.BAD_REQUEST.getReasonPhrase(),
              e.getMessage(),
              caminho,
              e.getCampos());
      return resposta(HttpStatus.BAD_REQUEST, body);
    } catch (RecursoNaoEncontradoException e) {
      return erro(HttpStatus.NOT_FOUND, e.getMessage(), caminho);
    } catch (ErroDePersistenciaException e) {
      return erro(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao acessar o banco de dados", caminho);
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> ResponseEntity<T> erro(HttpStatus status, String mensagem, String caminho) {
    ApiError body =
        new ApiError(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            mensagem,
            caminho,
            Map.of());
    return resposta(status, body);
  }

  @SuppressWarnings("unchecked")
  private static <T> ResponseEntity<T> resposta(HttpStatus status, ApiError body) {
    return (ResponseEntity<T>) ResponseEntity.status(status).body(body);
  }
}
