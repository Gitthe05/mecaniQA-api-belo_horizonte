package br.com.mecaniqa.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.mecaniqa.api.dto.AtualizarServicoRequest;
import br.com.mecaniqa.api.dto.CriarServicoRequest;
import br.com.mecaniqa.api.dto.ServicoResponse;
import br.com.mecaniqa.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniqa.api.mapper.ServicoMapper;
import br.com.mecaniqa.api.model.Servico;
import br.com.mecaniqa.api.repository.ServicoRepository;

@Service
public class ServicoService {

	private final ServicoRepository repository;

	public ServicoService() {
		this.repository = ServicoRepository.getInstance();
	}

	public ServicoResponse criar(CriarServicoRequest request) {
		Servico servico = repository.salvar(ServicoMapper.toEntity(request));
		return ServicoMapper.toResponse(servico);
	}

	public List<ServicoResponse> listar() {
		return repository.listarTodos().stream()
				.map(ServicoMapper::toResponse)
				.toList();
	}

	public ServicoResponse buscar(Long codigo) {
		return ServicoMapper.toResponse(buscarEntidade(codigo));
	}

	public ServicoResponse atualizar(Long codigo, AtualizarServicoRequest request) {
		Servico servico = repository.atualizar(
				codigo,
				request.getDuracaoEstimadaMinutos(),
				request.getCustoTabelado())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado"));
		return ServicoMapper.toResponse(servico);
	}

	public void excluir(Long codigo) {
		if (!repository.excluir(codigo)) {
			throw new RecursoNaoEncontradoException("Serviço não encontrado");
		}
	}

	private Servico buscarEntidade(Long codigo) {
		return repository.buscarPorCodigo(codigo)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado"));
	}
}
