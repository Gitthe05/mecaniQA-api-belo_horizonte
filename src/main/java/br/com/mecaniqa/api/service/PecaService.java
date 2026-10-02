package br.com.mecaniqa.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.mecaniqa.api.dto.AtualizarPecaRequest;
import br.com.mecaniqa.api.dto.CriarPecaRequest;
import br.com.mecaniqa.api.dto.PecaResponse;
import br.com.mecaniqa.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniqa.api.mapper.PecaMapper;
import br.com.mecaniqa.api.model.Peca;
import br.com.mecaniqa.api.repository.PecaRepository;

@Service
public class PecaService {

	private final PecaRepository repository;

	public PecaService() {
		this.repository = PecaRepository.getInstance();
	}

	public PecaResponse criar(CriarPecaRequest request) {
		Peca peca = repository.salvar(PecaMapper.toEntity(request));
		return PecaMapper.toResponse(peca);
	}

	public List<PecaResponse> listar() {
		return repository.listarTodos().stream()
				.map(PecaMapper::toResponse)
				.toList();
	}

	public PecaResponse buscar(Long codigo) {
		return PecaMapper.toResponse(buscarEntidade(codigo));
	}

	public PecaResponse atualizar(Long codigo, AtualizarPecaRequest request) {
		Peca peca = repository.atualizar(
				codigo,
				request.getQuantidadeEstoque(),
				request.getPrecoCusto(),
				request.getPrecoVenda())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada"));
		return PecaMapper.toResponse(peca);
	}

	public void excluir(Long codigo) {
		if (!repository.excluir(codigo)) {
			throw new RecursoNaoEncontradoException("Peça não encontrada");
		}
	}

	private Peca buscarEntidade(Long codigo) {
		return repository.buscarPorCodigo(codigo)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada"));
	}
}
