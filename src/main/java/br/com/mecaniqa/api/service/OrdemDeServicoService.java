package br.com.mecaniqa.api.service;

import br.com.mecaniqa.api.dto.AtualizarStatusOrdemServicoRequest;
import br.com.mecaniqa.api.dto.CriarOrdemServicoRequest;
import br.com.mecaniqa.api.dto.OrdemServicoResponse;
import br.com.mecaniqa.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniqa.api.mapper.OrdemServicoMapper;
import br.com.mecaniqa.api.model.OrdemDeServico;
import br.com.mecaniqa.api.model.Peca;
import br.com.mecaniqa.api.model.Servico;
import br.com.mecaniqa.api.repository.OrdemServicoRepository;
import br.com.mecaniqa.api.repository.PecaJdbcRepository;
import br.com.mecaniqa.api.repository.ServicoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrdemDeServicoService {

  private final OrdemServicoRepository ordemRepository;
  private final PecaJdbcRepository pecaRepository;
  private final ServicoRepository servicoRepository;

  public OrdemDeServicoService(
      OrdemServicoRepository ordemRepository,
      PecaJdbcRepository pecaRepository,
      ServicoRepository servicoRepository) {
    this.ordemRepository = ordemRepository;
    this.pecaRepository = pecaRepository;
    this.servicoRepository = servicoRepository;
  }

  public OrdemServicoResponse criar(CriarOrdemServicoRequest request) {
    List<Peca> pecas = request.getCodigosPecas().stream().map(this::buscarPeca).toList();
    List<Servico> servicos =
        request.getCodigosServicos().stream().map(this::buscarServico).toList();
    OrdemDeServico ordem = OrdemServicoMapper.toEntity(request, pecas, servicos);
    return OrdemServicoMapper.toResponse(ordemRepository.salvar(ordem));
  }

  public List<OrdemServicoResponse> listar() {
    return ordemRepository.listarTodos().stream().map(OrdemServicoMapper::toResponse).toList();
  }

  public OrdemServicoResponse buscar(Long codigo) {
    return OrdemServicoMapper.toResponse(buscarOrdem(codigo));
  }

  public OrdemServicoResponse atualizarStatus(
      Long codigo, AtualizarStatusOrdemServicoRequest request) {
    OrdemDeServico ordem =
        ordemRepository
            .atualizarStatus(codigo, request.getStatus())
            .orElseThrow(
                () -> new RecursoNaoEncontradoException("Ordem de serviço não encontrada"));
    return OrdemServicoMapper.toResponse(ordem);
  }

  public void excluir(Long codigo) {
    if (!ordemRepository.excluir(codigo))
      throw new RecursoNaoEncontradoException("Ordem de serviço não encontrada");
  }

  private OrdemDeServico buscarOrdem(Long codigo) {
    return ordemRepository
        .buscarPorCodigo(codigo)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de serviço não encontrada"));
  }

  private Peca buscarPeca(Long codigo) {
    return pecaRepository
        .buscarPorCodigo(codigo)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada: " + codigo));
  }

  private Servico buscarServico(Long codigo) {
    return servicoRepository
        .buscarPorCodigo(codigo)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado: " + codigo));
  }
}
