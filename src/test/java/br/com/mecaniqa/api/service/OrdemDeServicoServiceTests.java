package br.com.mecaniqa.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.mecaniqa.api.dto.CriarOrdemServicoRequest;
import br.com.mecaniqa.api.dto.OrdemServicoResponse;
import br.com.mecaniqa.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniqa.api.model.CategoriaPeca;
import br.com.mecaniqa.api.model.OrdemDeServico;
import br.com.mecaniqa.api.model.Peca;
import br.com.mecaniqa.api.model.Servico;
import br.com.mecaniqa.api.repository.OrdemServicoRepository;
import br.com.mecaniqa.api.repository.PecaJdbcRepository;
import br.com.mecaniqa.api.repository.ServicoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrdemDeServicoServiceTests {
  private OrdemServicoRepository ordens;
  private PecaJdbcRepository pecas;
  private ServicoRepository servicos;
  private OrdemDeServicoService service;

  @BeforeEach
  void setup() {
    ordens = org.mockito.Mockito.mock(OrdemServicoRepository.class);
    pecas = org.mockito.Mockito.mock(PecaJdbcRepository.class);
    servicos = org.mockito.Mockito.mock(ServicoRepository.class);
    service = new OrdemDeServicoService(ordens, pecas, servicos);
  }

  @Test
  void criaOrdemComPecasEServicosEncontrados() {
    Peca peca = novaPeca(7L);
    Servico servico = new Servico("Troca", 30, new BigDecimal("80.00"));
    servico.setCodigo(9L);
    when(pecas.buscarPorCodigo(7L)).thenReturn(Optional.of(peca));
    when(servicos.buscarPorCodigo(9L)).thenReturn(Optional.of(servico));
    when(ordens.salvar(any(OrdemDeServico.class)))
        .thenAnswer(
            invocation -> {
              OrdemDeServico ordem = invocation.getArgument(0);
              ordem.setCodigo(20L);
              ordem.setDataCriacao(LocalDateTime.now());
              ordem.setDataUltimaAtualizacao(ordem.getDataCriacao());
              return ordem;
            });

    CriarOrdemServicoRequest request = new CriarOrdemServicoRequest();
    request.setCliente("Ana");
    request.setVeiculo("ABC-1234");
    request.setDescricao("Troca de pastilhas");
    request.setCodigosPecas(java.util.List.of(7L));
    request.setCodigosServicos(java.util.List.of(9L));

    OrdemServicoResponse response = service.criar(request);

    assertEquals(20L, response.getCodigo());
    assertEquals("ABERTO", response.getStatus().name());
    assertEquals(7L, response.getPecas().get(0).getCodigo());
    assertEquals(9L, response.getServicos().get(0).getCodigo());
  }

  @Test
  void naoCriaOrdemQuandoUmaPecaNaoExiste() {
    when(pecas.buscarPorCodigo(999L)).thenReturn(Optional.empty());
    CriarOrdemServicoRequest request = new CriarOrdemServicoRequest();
    request.setCliente("Ana");
    request.setVeiculo("ABC-1234");
    request.setDescricao("Reparo");
    request.setCodigosPecas(java.util.List.of(999L));

    assertThrows(RecursoNaoEncontradoException.class, () -> service.criar(request));
    verify(ordens, never()).salvar(any(OrdemDeServico.class));
  }

  private Peca novaPeca(Long codigo) {
    Peca peca =
        new Peca(
            "789",
            "Bosch",
            2,
            BigDecimal.TEN,
            new BigDecimal("15.00"),
            null,
            null,
            CategoriaPeca.FREIOS);
    peca.setCodigo(codigo);
    return peca;
  }
}
