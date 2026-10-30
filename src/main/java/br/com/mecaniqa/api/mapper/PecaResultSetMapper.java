package br.com.mecaniqa.api.mapper;

import br.com.mecaniqa.api.model.CategoriaPeca;
import br.com.mecaniqa.api.model.Peca;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** Converte uma linha SQL da tabela pecas em objeto de domínio. */
public final class PecaResultSetMapper {
  private PecaResultSetMapper() {}

  public static Peca mapear(ResultSet resultado) throws SQLException {
    Peca peca =
        new Peca(
            resultado.getString("codigo_barras"),
            resultado.getString("fornecedor_marca"),
            resultado.getInt("quantidade_estoque"),
            resultado.getBigDecimal("preco_custo"),
            resultado.getBigDecimal("preco_venda"),
            resultado.getString("tamanho"),
            resultado.getString("cor"),
            CategoriaPeca.valueOf(resultado.getString("categoria")));
    peca.setCodigo(resultado.getLong("codigo"));
    Timestamp cadastro = resultado.getTimestamp("data_cadastro");
    Timestamp atualizacao = resultado.getTimestamp("data_ultima_atualizacao");
    peca.setDataCadastro(cadastro == null ? null : cadastro.toLocalDateTime());
    peca.setDataUltimaAtualizacao(atualizacao == null ? null : atualizacao.toLocalDateTime());
    return peca;
  }
}
