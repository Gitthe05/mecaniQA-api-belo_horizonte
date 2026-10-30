package br.com.mecaniqa.api.mapper;

import br.com.mecaniqa.api.model.PedidoPecas;
import br.com.mecaniqa.api.model.StatusPedidoPecas;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class PedidoPecasResultSetMapper {
  private PedidoPecasResultSetMapper() {}

  public static PedidoPecas mapear(ResultSet rs) throws SQLException {
    PedidoPecas p = new PedidoPecas(rs.getString("fornecedor"));
    p.setCodigo(rs.getLong("codigo"));
    p.setStatus(StatusPedidoPecas.valueOf(rs.getString("status")));
    p.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
    p.setDataUltimaAtualizacao(rs.getTimestamp("data_ultima_atualizacao").toLocalDateTime());
    return p;
  }
}
