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

import br.com.mecaniqa.api.dto.AtualizarPecaRequest;
import br.com.mecaniqa.api.dto.CriarPecaRequest;
import br.com.mecaniqa.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniqa.api.model.Peca;
import br.com.mecaniqa.api.repository.PecaRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {

	private final PecaRepository repository = PecaRepository.getInstance();

	@PostMapping
	public ResponseEntity<Peca> criar(@Valid @RequestBody CriarPecaRequest request) {
		Peca peca = new Peca(
				request.getCodigoBarras(),
				request.getFornecedorMarca(),
				request.getQuantidadeEstoque(),
				request.getPrecoCusto(),
				request.getPrecoVenda(),
				request.getTamanho(),
				request.getCor(),
				request.getCategoria());

		Peca pecaCriada = repository.salvar(peca);
		URI localizacao = URI.create("/api/pecas/" + pecaCriada.getCodigo());
		return ResponseEntity.created(localizacao).body(pecaCriada);
	}

	@GetMapping
	public ResponseEntity<List<Peca>> listar() {
		return ResponseEntity.ok(repository.listarTodos());
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Peca> buscar(@PathVariable Long codigo) {
		Peca peca = repository.buscarPorCodigo(codigo)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada"));
		return ResponseEntity.ok(peca);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Peca> atualizar(
			@PathVariable Long codigo,
			@Valid @RequestBody AtualizarPecaRequest request) {
		Peca peca = repository.atualizar(
				codigo,
				request.getQuantidadeEstoque(),
				request.getPrecoCusto(),
				request.getPrecoVenda())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada"));
		return ResponseEntity.ok(peca);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> excluir(@PathVariable Long codigo) {
		if (!repository.excluir(codigo)) {
			throw new RecursoNaoEncontradoException("Peça não encontrada");
		}
		return ResponseEntity.noContent().build();
	}
}
