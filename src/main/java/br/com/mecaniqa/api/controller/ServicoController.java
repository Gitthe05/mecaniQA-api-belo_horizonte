package br.com.mecaniqa.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mecaniqa.api.dto.AtualizarServicoRequest;
import br.com.mecaniqa.api.dto.CriarServicoRequest;
import br.com.mecaniqa.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniqa.api.model.Servico;
import br.com.mecaniqa.api.repository.ServicoRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

	private final ServicoRepository repository = ServicoRepository.getInstance();

	@PostMapping
	public ResponseEntity<Servico> criar(@Valid @RequestBody CriarServicoRequest request) {
		Servico servico = new Servico(
				request.getNome(),
				request.getDuracaoEstimadaMinutos(),
				request.getCustoTabelado());

		Servico servicoCriado = repository.salvar(servico);
		URI localizacao = URI.create("/api/servicos/" + servicoCriado.getCodigo());
		return ResponseEntity.created(localizacao).body(servicoCriado);
	}

	@GetMapping
	public ResponseEntity<List<Servico>> listar() {
		return ResponseEntity.ok(repository.listarTodos());
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Servico> buscar(@PathVariable Long codigo) {
		Servico servico = repository.buscarPorCodigo(codigo)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado"));
		return ResponseEntity.ok(servico);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Servico> atualizar(
			@PathVariable Long codigo,
			@Valid @RequestBody AtualizarServicoRequest request) {
		Servico servico = repository.atualizar(
				codigo,
				request.getDuracaoEstimadaMinutos(),
				request.getCustoTabelado())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado"));
		return ResponseEntity.ok(servico);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> excluir(@PathVariable Long codigo) {
		if (!repository.excluir(codigo)) {
			throw new RecursoNaoEncontradoException("Serviço não encontrado");
		}
		return ResponseEntity.noContent().build();
	}
}
