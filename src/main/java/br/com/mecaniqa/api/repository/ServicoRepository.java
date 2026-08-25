package br.com.mecaniqa.api.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import br.com.mecaniqa.api.model.Servico;

public final class ServicoRepository {

	private static final ServicoRepository INSTANCE = new ServicoRepository();

	private final List<Servico> servicos = new ArrayList<>();
	private final AtomicLong proximoCodigo = new AtomicLong(1);

	private ServicoRepository() {
	}

	public static ServicoRepository getInstance() {
		return INSTANCE;
	}

	public synchronized Servico salvar(Servico servico) {
		LocalDateTime agora = LocalDateTime.now();
		servico.setCodigo(proximoCodigo.getAndIncrement());
		servico.setDataCriacao(agora);
		servico.setDataUltimaAtualizacao(agora);
		servicos.add(servico);
		return servico;
	}

	public synchronized List<Servico> listarTodos() {
		return List.copyOf(servicos);
	}

	public synchronized Optional<Servico> buscarPorCodigo(Long codigo) {
		return servicos.stream()
				.filter(servico -> servico.getCodigo().equals(codigo))
				.findFirst();
	}

	public synchronized Optional<Servico> atualizar(
			Long codigo,
			Integer duracaoEstimadaMinutos,
			BigDecimal custoTabelado) {
		Optional<Servico> servicoEncontrado = buscarPorCodigo(codigo);
		servicoEncontrado.ifPresent(servico -> {
			servico.setDuracaoEstimadaMinutos(duracaoEstimadaMinutos);
			servico.setCustoTabelado(custoTabelado);
			servico.setDataUltimaAtualizacao(LocalDateTime.now());
		});
		return servicoEncontrado;
	}

	public synchronized boolean excluir(Long codigo) {
		return servicos.removeIf(servico -> servico.getCodigo().equals(codigo));
	}
}
