package br.com.mecaniqa.api.repository;

import br.com.mecaniqa.api.config.ConexaoMySQL;
import br.com.mecaniqa.api.mapper.PecaResultSetMapper;
import br.com.mecaniqa.api.model.Peca;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/** Repositório JDBC de peças. O template controla a conexão e as exceções. */
@Repository
public class PecaJdbcRepository extends RepositorioJdbcTemplate implements Repositorio<Peca, Long> {
  private static final String COLUNAS =
      "codigo,codigo_barras,fornecedor_marca,quantidade_estoque,preco_custo,preco_venda,data_cadastro,data_ultima_atualizacao,tamanho,cor,categoria";

  public PecaJdbcRepository(ConexaoMySQL conexao) {
    super(conexao);
  }

  @Override
  public Peca salvar(Peca p) {
    return executar(
        "salvar peça",
        c -> {
          try (var ps =
              c.prepareStatement(
                  "INSERT INTO"
                      + " pecas(codigo_barras,fornecedor_marca,quantidade_estoque,preco_custo,preco_venda,tamanho,cor,categoria)"
                      + " VALUES(?,?,?,?,?,?,?,?)",
                  Statement.RETURN_GENERATED_KEYS)) {
            preencher(ps, p);
            ps.executeUpdate();
            try (var k = ps.getGeneratedKeys()) {
              if (!k.next()) throw new SQLException("Código da peça não retornado");
              p.setCodigo(k.getLong(1));
            }
          }
          return buscarPorCodigo(p.getCodigo())
              .orElseThrow(() -> new SQLException("Peça salva não foi encontrada"));
        });
  }

  @Override
  public Optional<Peca> buscarPorCodigo(Long id) {
    return buscarPorCodigo(
        id,
        "pecas",
        rs -> {
          try {
            return PecaResultSetMapper.mapear(rs);
          } catch (RuntimeException e) {
            throw e;
          }
        });
  }

  @Override
  public List<Peca> listarTodos() {
    return executar(
        "listar peças",
        c -> {
          List<Peca> out = new ArrayList<>();
          try (var ps = c.prepareStatement("SELECT " + COLUNAS + " FROM pecas ORDER BY codigo");
              var rs = ps.executeQuery()) {
            while (rs.next()) out.add(PecaResultSetMapper.mapear(rs));
          }
          return List.copyOf(out);
        });
  }

  public Optional<Peca> atualizar(Long id, Integer qtd, BigDecimal custo, BigDecimal venda) {
    return executar(
        "atualizar peça",
        c -> {
          try (var ps =
              c.prepareStatement(
                  "UPDATE pecas SET"
                      + " quantidade_estoque=?,preco_custo=?,preco_venda=?,data_ultima_atualizacao=CURRENT_TIMESTAMP"
                      + " WHERE codigo=?")) {
            ps.setInt(1, qtd);
            ps.setBigDecimal(2, custo);
            ps.setBigDecimal(3, venda);
            ps.setLong(4, id);
            if (ps.executeUpdate() == 0) return Optional.empty();
          }
          return buscarPorCodigo(id);
        });
  }

  @Override
  public Optional<Peca> atualizar(Long id, Peca p) {
    return atualizar(id, p.getQuantidadeEstoque(), p.getPrecoCusto(), p.getPrecoVenda());
  }

  @Override
  public boolean excluir(Long id) {
    return executar(
        "excluir peça",
        c -> {
          try (var ps = c.prepareStatement("DELETE FROM pecas WHERE codigo=?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
          }
        });
  }

  private static void preencher(java.sql.PreparedStatement ps, Peca p) throws SQLException {
    ps.setString(1, p.getCodigoBarras());
    ps.setString(2, p.getFornecedorMarca());
    ps.setInt(3, p.getQuantidadeEstoque());
    ps.setBigDecimal(4, p.getPrecoCusto());
    ps.setBigDecimal(5, p.getPrecoVenda());
    ps.setString(6, p.getTamanho());
    ps.setString(7, p.getCor());
    ps.setString(8, p.getCategoria().name());
  }
}
