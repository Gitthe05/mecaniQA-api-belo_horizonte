package br.com.mecaniqa.api.repository;

import br.com.mecaniqa.api.config.ConexaoMySQL;
import br.com.mecaniqa.api.mapper.PedidoPecasResultSetMapper;
import br.com.mecaniqa.api.model.ItemPedidoPeca;
import br.com.mecaniqa.api.model.Peca;
import br.com.mecaniqa.api.model.PedidoPecas;
import br.com.mecaniqa.api.model.StatusPedidoPecas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PedidoPecasRepository extends RepositorioJdbcTemplate
    implements Repositorio<PedidoPecas, Long> {
  private final PecaJdbcRepository pecas;

  public PedidoPecasRepository(ConexaoMySQL db, PecaJdbcRepository pecas) {
    super(db);
    this.pecas = pecas;
  }

  @Override
  public PedidoPecas salvar(PedidoPecas p) {
    return executar(
        "salvar pedido",
        c -> {
          try (var ps =
              c.prepareStatement(
                  "INSERT INTO pedidos_pecas(fornecedor,status) VALUES(?,?)",
                  Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getFornecedor());
            ps.setString(2, p.getStatus().name());
            ps.executeUpdate();
            try (var k = ps.getGeneratedKeys()) {
              if (!k.next()) throw new SQLException("Código do pedido não retornado");
              p.setCodigo(k.getLong(1));
            }
          }
          persistirItens(p);
          return buscarPorCodigo(p.getCodigo())
              .orElseThrow(() -> new SQLException("Pedido salvo não foi encontrado"));
        });
  }

  private void persistirItens(PedidoPecas p) throws SQLException {
    executar(
        "salvar itens do pedido",
        c -> {
          try (var ps =
              c.prepareStatement(
                  "INSERT INTO itens_pedido_pecas(pedido_codigo,peca_codigo,quantidade)"
                      + " VALUES(?,?,?) ON DUPLICATE KEY UPDATE quantidade=VALUES(quantidade)")) {
            for (ItemPedidoPeca i : p.getItens()) {
              ps.setLong(1, p.getCodigo());
              ps.setLong(2, i.getPeca().getCodigo());
              ps.setInt(3, i.getQuantidade());
              ps.addBatch();
            }
            ps.executeBatch();
          }
          return null;
        });
  }

  @Override
  public Optional<PedidoPecas> buscarPorCodigo(Long id) {
    return executar(
        "buscar pedido",
        c -> {
          try (var ps = c.prepareStatement("SELECT * FROM pedidos_pecas WHERE codigo=?")) {
            ps.setLong(1, id);
            try (var rs = ps.executeQuery()) {
              if (!rs.next()) return Optional.empty();
              PedidoPecas p = PedidoPecasResultSetMapper.mapear(rs);
              try (var itens =
                  c.prepareStatement(
                      "SELECT peca_codigo,quantidade FROM itens_pedido_pecas WHERE"
                          + " pedido_codigo=?")) {
                itens.setLong(1, id);
                try (var ir = itens.executeQuery()) {
                  while (ir.next()) {
                    Optional<Peca> peca = pecas.buscarPorCodigo(ir.getLong(1));
                    peca.ifPresent(value -> p.adicionarItem(value, irGetQuantidade(ir)));
                  }
                }
              }
              return Optional.of(p);
            }
          }
        });
  }

  private static int irGetQuantidade(ResultSet rs) {
    try {
      return rs.getInt("quantidade");
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public List<PedidoPecas> listarTodos() {
    return executar(
        "listar pedidos",
        c -> {
          List<Long> ids = new ArrayList<>();
          try (var ps = c.prepareStatement("SELECT codigo FROM pedidos_pecas ORDER BY codigo");
              var rs = ps.executeQuery()) {
            while (rs.next()) ids.add(rs.getLong(1));
          }
          List<PedidoPecas> all = new ArrayList<>();
          for (Long id : ids) buscarPorCodigo(id).ifPresent(all::add);
          return List.copyOf(all);
        });
  }

  public Optional<PedidoPecas> adicionarItem(Long id, Peca peca, Integer qtd) {
    return executar(
        "adicionar item ao pedido",
        c -> {
          try (var ps =
              c.prepareStatement(
                  "INSERT INTO itens_pedido_pecas(pedido_codigo,peca_codigo,quantidade)"
                      + " VALUES(?,?,?) ON DUPLICATE KEY UPDATE"
                      + " quantidade=quantidade+VALUES(quantidade)")) {
            ps.setLong(1, id);
            ps.setLong(2, peca.getCodigo());
            ps.setInt(3, qtd);
            if (ps.executeUpdate() == 0) return Optional.empty();
          }
          return buscarPorCodigo(id);
        });
  }

  public Optional<PedidoPecas> atualizarStatus(Long id, StatusPedidoPecas status) {
    return executar(
        "atualizar status do pedido",
        c -> {
          try (var ps =
              c.prepareStatement(
                  "UPDATE pedidos_pecas SET status=?,data_ultima_atualizacao=CURRENT_TIMESTAMP"
                      + " WHERE codigo=?")) {
            ps.setString(1, status.name());
            ps.setLong(2, id);
            if (ps.executeUpdate() == 0) return Optional.empty();
          }
          return buscarPorCodigo(id);
        });
  }

  @Override
  public Optional<PedidoPecas> atualizar(Long id, PedidoPecas p) {
    return atualizarStatus(id, p.getStatus());
  }

  @Override
  public boolean excluir(Long id) {
    return executar(
        "excluir pedido",
        c -> {
          try (var ps = c.prepareStatement("DELETE FROM pedidos_pecas WHERE codigo=?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
          }
        });
  }
}
