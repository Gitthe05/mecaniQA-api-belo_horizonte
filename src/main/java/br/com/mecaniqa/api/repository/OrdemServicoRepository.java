package br.com.mecaniqa.api.repository;

import br.com.mecaniqa.api.config.ConexaoMySQL;
import br.com.mecaniqa.api.mapper.OrdemServicoResultSetMapper;
import br.com.mecaniqa.api.model.OrdemDeServico;
import br.com.mecaniqa.api.model.Peca;
import br.com.mecaniqa.api.model.Servico;
import br.com.mecaniqa.api.model.StatusOrdemServico;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class OrdemServicoRepository extends RepositorioJdbcTemplate
    implements Repositorio<OrdemDeServico, Long> {
  private final PecaJdbcRepository pecas;
  private final ServicoRepository servicos;

  public OrdemServicoRepository(
      ConexaoMySQL db, PecaJdbcRepository pecas, ServicoRepository servicos) {
    super(db);
    this.pecas = pecas;
    this.servicos = servicos;
  }

  @Override
  public OrdemDeServico salvar(OrdemDeServico o) {
    return executar(
        "salvar ordem de serviço",
        c -> {
          try (var ps =
              c.prepareStatement(
                  "INSERT INTO ordens_servico(cliente,veiculo,descricao,status) VALUES(?,?,?,?)",
                  Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, o.getCliente());
            ps.setString(2, o.getVeiculo());
            ps.setString(3, o.getDescricao());
            ps.setString(4, o.getStatus().name());
            ps.executeUpdate();
            try (var k = ps.getGeneratedKeys()) {
              if (!k.next()) throw new SQLException("Código da OS não retornado");
              o.setCodigo(k.getLong(1));
            }
          }
          try (var ps =
              c.prepareStatement(
                  "INSERT INTO ordem_servico_pecas(ordem_codigo,peca_codigo) VALUES(?,?)")) {
            for (Peca p : o.getPecas()) {
              ps.setLong(1, o.getCodigo());
              ps.setLong(2, p.getCodigo());
              ps.addBatch();
            }
            ps.executeBatch();
          }
          try (var ps =
              c.prepareStatement(
                  "INSERT INTO ordem_servico_servicos(ordem_codigo,servico_codigo) VALUES(?,?)")) {
            for (Servico s : o.getServicos()) {
              ps.setLong(1, o.getCodigo());
              ps.setLong(2, s.getCodigo());
              ps.addBatch();
            }
            ps.executeBatch();
          }
          return buscarPorCodigo(o.getCodigo())
              .orElseThrow(() -> new SQLException("OS salva não foi encontrada"));
        });
  }

  @Override
  public Optional<OrdemDeServico> buscarPorCodigo(Long id) {
    return executar(
        "buscar ordem",
        c -> {
          try (var ps = c.prepareStatement("SELECT * FROM ordens_servico WHERE codigo=?")) {
            ps.setLong(1, id);
            try (var rs = ps.executeQuery()) {
              if (!rs.next()) return Optional.empty();
              OrdemDeServico o = OrdemServicoResultSetMapper.mapear(rs);
              o.setPecas(
                  ids(c, "ordem_servico_pecas", "peca_codigo", id).stream()
                      .map(pecas::buscarPorCodigo)
                      .flatMap(Optional::stream)
                      .toList());
              o.setServicos(
                  ids(c, "ordem_servico_servicos", "servico_codigo", id).stream()
                      .map(servicos::buscarPorCodigo)
                      .flatMap(Optional::stream)
                      .toList());
              return Optional.of(o);
            }
          }
        });
  }

  @Override
  public List<OrdemDeServico> listarTodos() {
    return executar(
        "listar ordens",
        c -> {
          List<Long> ids = new ArrayList<>();
          try (var ps = c.prepareStatement("SELECT codigo FROM ordens_servico ORDER BY codigo");
              var rs = ps.executeQuery()) {
            while (rs.next()) ids.add(rs.getLong(1));
          }
          List<OrdemDeServico> out = new ArrayList<>();
          for (Long id : ids) buscarPorCodigo(id).ifPresent(out::add);
          return List.copyOf(out);
        });
  }

  public Optional<OrdemDeServico> atualizarStatus(Long id, StatusOrdemServico status) {
    return executar(
        "atualizar status da OS",
        c -> {
          try (var ps =
              c.prepareStatement(
                  "UPDATE ordens_servico SET status=?,data_ultima_atualizacao=CURRENT_TIMESTAMP"
                      + " WHERE codigo=?")) {
            ps.setString(1, status.name());
            ps.setLong(2, id);
            if (ps.executeUpdate() == 0) return Optional.empty();
          }
          return buscarPorCodigo(id);
        });
  }

  @Override
  public Optional<OrdemDeServico> atualizar(Long id, OrdemDeServico o) {
    return atualizarStatus(id, o.getStatus());
  }

  @Override
  public boolean excluir(Long id) {
    return executar(
        "excluir ordem",
        c -> {
          try (var ps = c.prepareStatement("DELETE FROM ordens_servico WHERE codigo=?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
          }
        });
  }

  private static List<Long> ids(java.sql.Connection c, String tabela, String coluna, long id)
      throws SQLException {
    List<Long> out = new ArrayList<>();
    try (var ps =
        c.prepareStatement("SELECT " + coluna + " FROM " + tabela + " WHERE ordem_codigo=?")) {
      ps.setLong(1, id);
      try (var rs = ps.executeQuery()) {
        while (rs.next()) out.add(rs.getLong(1));
      }
    }
    return out;
  }
}
