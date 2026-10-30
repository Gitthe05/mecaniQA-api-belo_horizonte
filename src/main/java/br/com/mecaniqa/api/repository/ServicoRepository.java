package br.com.mecaniqa.api.repository;

import br.com.mecaniqa.api.config.ConexaoMySQL;
import br.com.mecaniqa.api.mapper.ServicoResultSetMapper;
import br.com.mecaniqa.api.model.Servico;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ServicoRepository extends RepositorioJdbcTemplate
    implements Repositorio<Servico, Long> {
  public ServicoRepository(ConexaoMySQL conexao) {
    super(conexao);
  }

  @Override
  public Servico salvar(Servico s) {
    return executar(
        "salvar serviço",
        c -> {
          try (var ps =
              c.prepareStatement(
                  "INSERT INTO servicos (nome,duracao_estimada_minutos,custo_tabelado) VALUES"
                      + " (?,?,?)",
                  Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getNome());
            ps.setInt(2, s.getDuracaoEstimadaMinutos());
            ps.setBigDecimal(3, s.getCustoTabelado());
            ps.executeUpdate();
            try (var keys = ps.getGeneratedKeys()) {
              if (!keys.next()) throw new SQLException("Código do serviço não retornado");
              s.setCodigo(keys.getLong(1));
            }
          }
          return buscarPorCodigo(s.getCodigo())
              .orElseThrow(() -> new SQLException("Serviço salvo não foi encontrado"));
        });
  }

  @Override
  public Optional<Servico> buscarPorCodigo(Long id) {
    return buscarPorCodigo(id, "servicos", ServicoResultSetMapper::mapear);
  }

  @Override
  public List<Servico> listarTodos() {
    return executar(
        "listar serviços",
        c -> {
          List<Servico> out = new ArrayList<>();
          try (var ps = c.prepareStatement("SELECT * FROM servicos ORDER BY codigo");
              var rs = ps.executeQuery()) {
            while (rs.next()) out.add(ServicoResultSetMapper.mapear(rs));
          }
          return List.copyOf(out);
        });
  }

  public Optional<Servico> atualizar(Long id, Integer duracao, BigDecimal custo) {
    return executar(
        "atualizar serviço",
        c -> {
          try (var ps =
              c.prepareStatement(
                  "UPDATE servicos SET"
                      + " duracao_estimada_minutos=?,custo_tabelado=?,data_ultima_atualizacao=CURRENT_TIMESTAMP"
                      + " WHERE codigo=?")) {
            ps.setInt(1, duracao);
            ps.setBigDecimal(2, custo);
            ps.setLong(3, id);
            if (ps.executeUpdate() == 0) return Optional.empty();
          }
          return buscarPorCodigo(id);
        });
  }

  @Override
  public Optional<Servico> atualizar(Long id, Servico s) {
    return executar(
        "atualizar serviço",
        c -> {
          try (var ps =
              c.prepareStatement(
                  "UPDATE servicos SET"
                      + " nome=?,duracao_estimada_minutos=?,custo_tabelado=?,data_ultima_atualizacao=CURRENT_TIMESTAMP"
                      + " WHERE codigo=?")) {
            ps.setString(1, s.getNome());
            ps.setInt(2, s.getDuracaoEstimadaMinutos());
            ps.setBigDecimal(3, s.getCustoTabelado());
            ps.setLong(4, id);
            if (ps.executeUpdate() == 0) return Optional.empty();
          }
          return buscarPorCodigo(id);
        });
  }

  @Override
  public boolean excluir(Long id) {
    return executar(
        "excluir serviço",
        c -> {
          try (var ps = c.prepareStatement("DELETE FROM servicos WHERE codigo=?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
          }
        });
  }
}
