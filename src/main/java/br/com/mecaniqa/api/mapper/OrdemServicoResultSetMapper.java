package br.com.mecaniqa.api.mapper;

import br.com.mecaniqa.api.model.OrdemDeServico;
import br.com.mecaniqa.api.model.StatusOrdemServico;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class OrdemServicoResultSetMapper {
  private OrdemServicoResultSetMapper() {}

  public static OrdemDeServico mapear(ResultSet rs) throws SQLException {
    OrdemDeServico o =
        OrdemDeServico.builder()
            .cliente(rs.getString("cliente"))
            .veiculo(rs.getString("veiculo"))
            .descricao(rs.getString("descricao"))
            .status(StatusOrdemServico.valueOf(rs.getString("status")))
            .build();
    o.setCodigo(rs.getLong("codigo"));
    o.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
    o.setDataUltimaAtualizacao(rs.getTimestamp("data_ultima_atualizacao").toLocalDateTime());
    return o;
  }
}
