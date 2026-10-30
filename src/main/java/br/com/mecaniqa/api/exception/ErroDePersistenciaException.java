package br.com.mecaniqa.api.exception;

public class ErroDePersistenciaException extends RuntimeException {
  public ErroDePersistenciaException(String message, Throwable cause) {
    super(message, cause);
  }
}
