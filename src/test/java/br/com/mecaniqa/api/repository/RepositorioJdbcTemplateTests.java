package br.com.mecaniqa.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import br.com.mecaniqa.api.config.ConexaoMySQL;
import br.com.mecaniqa.api.exception.ErroDePersistenciaException;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

class RepositorioJdbcTemplateTests {
  @Test
  void conexaoFechaQuandoOperacaoTemSucesso() throws Exception {
    ConexaoMySQL db = mock(ConexaoMySQL.class);
    Connection c = mock(Connection.class);
    when(db.obterConexao()).thenReturn(c);
    assertEquals("ok", new Template(db).rodar(false));
    verify(c).close();
  }

  @Test
  void conexaoFechaQuandoQueryFalha() throws Exception {
    ConexaoMySQL db = mock(ConexaoMySQL.class);
    Connection c = mock(Connection.class);
    when(db.obterConexao()).thenReturn(c);
    assertThrows(ErroDePersistenciaException.class, () -> new Template(db).rodar(true));
    verify(c).close();
  }

  private static final class Template extends RepositorioJdbcTemplate {
    Template(ConexaoMySQL db) {
      super(db);
    }

    String rodar(boolean falha) {
      return executar(
          "testar",
          c -> {
            if (falha) throw new SQLException("falha de teste");
            return "ok";
          });
    }
  }
}
