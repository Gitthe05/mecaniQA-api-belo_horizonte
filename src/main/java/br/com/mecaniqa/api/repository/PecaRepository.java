package br.com.mecaniqa.api.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import br.com.mecaniqa.api.model.Peca;

public final class PecaRepository {

	private static final PecaRepository INSTANCE = new PecaRepository();

	private final List<Peca> pecas = new ArrayList<>();
	private final AtomicLong proximoCodigo = new AtomicLong(1);

	private PecaRepository() {
	}

	public static PecaRepository getInstance() {
		return INSTANCE;
	}

	public synchronized Peca salvar(Peca peca) {
		LocalDateTime agora = LocalDateTime.now();
		peca.setCodigo(proximoCodigo.getAndIncrement());
		peca.setDataCadastro(agora);
		peca.setDataUltimaAtualizacao(agora);
		pecas.add(peca);
		return peca;
	}

	public synchronized List<Peca> listarTodos() {
		return List.copyOf(pecas);
	}

	public synchronized Optional<Peca> buscarPorCodigo(Long codigo) {
		return pecas.stream()
				.filter(peca -> peca.getCodigo().equals(codigo))
				.findFirst();
	}

	public synchronized Optional<Peca> atualizar(
			Long codigo,
			Integer quantidadeEstoque,
			BigDecimal precoCusto,
			BigDecimal precoVenda) {
		Optional<Peca> pecaEncontrada = buscarPorCodigo(codigo);
		pecaEncontrada.ifPresent(peca -> {
			peca.setQuantidadeEstoque(quantidadeEstoque);
			peca.setPrecoCusto(precoCusto);
			peca.setPrecoVenda(precoVenda);
			peca.setDataUltimaAtualizacao(LocalDateTime.now());
		});
		return pecaEncontrada;
	}

	public synchronized boolean excluir(Long codigo) {
		return pecas.removeIf(peca -> peca.getCodigo().equals(codigo));
	}
}
