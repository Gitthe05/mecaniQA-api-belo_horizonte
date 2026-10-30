package br.com.mecaniqa.api.config;

import br.com.mecaniqa.api.exception.ErroDePersistenciaException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Fornece conexões JDBC sem ORM ou camada de persistência automática. */
@Component
public class ConexaoMySQL {

  private final String url;
  private final String usuario;
  private final String senha;

  public ConexaoMySQL(
      @Value("${db.url}") String url,
      @Value("${db.username}") String usuario,
      @Value("${db.password}") String senha) {
    this.url = url;
    this.usuario = usuario;
    this.senha = senha;
  }

  public Connection obterConexao() {
    try {
      return DriverManager.getConnection(url, usuario, senha);
    } catch (SQLException exception) {
      throw new ErroDePersistenciaException(
          "Não foi possível conectar ao MySQL. Verifique DB_URL, DB_USER, DB_PASSWORD e se o"
              + " servidor está ativo.",
          exception);
    }
  }
}
