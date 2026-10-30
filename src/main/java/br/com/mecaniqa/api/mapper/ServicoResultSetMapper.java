package br.com.mecaniqa.api.mapper;

import br.com.mecaniqa.api.model.Servico;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class ServicoResultSetMapper {
  private ServicoResultSetMapper() {}

  public static Servico mapear(ResultSet rs) throws SQLException {
    Servico s = new Servico();
    s.setCodigo(rs.getLong("codigo"));
    s.setNome(rs.getString("nome"));
    s.setDuracaoEstimadaMinutos(rs.getInt("duracao_estimada_minutos"));
    s.setCustoTabelado(rs.getBigDecimal("custo_tabelado"));
    s.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
    s.setDataUltimaAtualizacao(rs.getTimestamp("data_ultima_atualizacao").toLocalDateTime());
    return s;
  }
}
