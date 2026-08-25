package br.com.mecaniqa.api.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RecursoNaoEncontradoException.class)
	public ResponseEntity<ApiError> tratarRecursoNaoEncontrado(
			RecursoNaoEncontradoException exception,
			HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		ApiError erro = new ApiError(
				LocalDateTime.now(),
				status.value(),
				status.getReasonPhrase(),
				exception.getMessage(),
				request.getRequestURI(),
				Map.of());
		return ResponseEntity.status(status).body(erro);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> tratarValidacao(
			MethodArgumentNotValidException exception,
			HttpServletRequest request) {
		Map<String, String> campos = new LinkedHashMap<>();
		exception.getBindingResult().getFieldErrors().forEach(erro ->
				campos.putIfAbsent(erro.getField(), erro.getDefaultMessage()));

		return respostaBadRequest("Dados de entrada inválidos", request.getRequestURI(), campos);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> tratarCorpoInvalido(
			HttpMessageNotReadableException exception,
			HttpServletRequest request) {
		return respostaBadRequest("Corpo da requisição inválido", request.getRequestURI(), Map.of());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiError> tratarParametroInvalido(
			MethodArgumentTypeMismatchException exception,
			HttpServletRequest request) {
		Map<String, String> campos = Map.of(exception.getName(), "possui formato inválido");
		return respostaBadRequest("Parâmetro de rota inválido", request.getRequestURI(), campos);
	}

	private ResponseEntity<ApiError> respostaBadRequest(
			String mensagem,
			String caminho,
			Map<String, String> campos) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ApiError erro = new ApiError(
				LocalDateTime.now(),
				status.value(),
				status.getReasonPhrase(),
				mensagem,
				caminho,
				campos);
		return ResponseEntity.status(status).body(erro);
	}
}
