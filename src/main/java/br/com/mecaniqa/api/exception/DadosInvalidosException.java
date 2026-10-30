package br.com.mecaniqa.api.exception;

import java.util.Map;

public class DadosInvalidosException extends RuntimeException {
  private final Map<String, String> campos;

  public DadosInvalidosException(Map<String, String> campos) {
    super("Corpo da requisição inválido");
    this.campos = Map.copyOf(campos);
  }

  public Map<String, String> getCampos() {
    return campos;
  }
}
