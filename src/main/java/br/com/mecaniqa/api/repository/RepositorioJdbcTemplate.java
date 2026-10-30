package br.com.mecaniqa.api.repository;

import br.com.mecaniqa.api.config.ConexaoMySQL;
import br.com.mecaniqa.api.exception.ErroDePersistenciaException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/** Template Method para garantir abertura, tratamento de falhas e fechamento JDBC. */
public abstract class RepositorioJdbcTemplate {
  private final ConexaoMySQL conexao;

  protected RepositorioJdbcTemplate(ConexaoMySQL conexao) {
    this.conexao = conexao;
  }

  protected final <T> T executar(String operacao, OperacaoJdbc<T> rotina) {
    try (Connection connection = conexao.obterConexao()) {
      return rotina.executar(connection);
    } catch (SQLException exception) {
      throw new ErroDePersistenciaException(
          "Falha ao " + operacao + " no MySQL: " + exception.getMessage(), exception);
    }
  }

  protected final <T, ID> Optional<T> buscarPorCodigo(
      ID codigo, String tabela, RowMapper<T> mapper) {
    return executar(
        "buscar registro",
        connection -> {
          try (var statement =
              connection.prepareStatement("SELECT * FROM " + tabela + " WHERE codigo = ?")) {
            statement.setObject(1, codigo);
            try (var result = statement.executeQuery()) {
              return result.next() ? Optional.of(mapper.map(result)) : Optional.empty();
            }
          }
        });
  }

  @FunctionalInterface
  protected interface RowMapper<T> {
    T map(java.sql.ResultSet result) throws SQLException;
  }

  @FunctionalInterface
  protected interface OperacaoJdbc<T> {
    T executar(Connection connection) throws SQLException;
  }
}
