package br.com.mecaniqa.api.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import br.com.mecaniqa.api.model.OrdemDeServico;
import br.com.mecaniqa.api.model.StatusOrdemServico;

public final class OrdemServicoRepository {

	private static final OrdemServicoRepository INSTANCE = new OrdemServicoRepository();
	private final List<OrdemDeServico> ordens = new ArrayList<>();
	private final AtomicLong proximoCodigo = new AtomicLong(1);

	private OrdemServicoRepository() {
	}

	public static OrdemServicoRepository getInstance() { return INSTANCE; }

	public synchronized OrdemDeServico salvar(OrdemDeServico ordem) {
		LocalDateTime agora = LocalDateTime.now();
		ordem.setCodigo(proximoCodigo.getAndIncrement());
		ordem.setDataCriacao(agora);
		ordem.setDataUltimaAtualizacao(agora);
		ordens.add(ordem);
		return ordem;
	}

	public synchronized List<OrdemDeServico> listarTodos() { return List.copyOf(ordens); }

	public synchronized Optional<OrdemDeServico> buscarPorCodigo(Long codigo) {
		return ordens.stream().filter(ordem -> ordem.getCodigo().equals(codigo)).findFirst();
	}

	public synchronized Optional<OrdemDeServico> atualizarStatus(Long codigo, StatusOrdemServico status) {
		Optional<OrdemDeServico> encontrada = buscarPorCodigo(codigo);
		encontrada.ifPresent(ordem -> {
			ordem.setStatus(status);
			ordem.setDataUltimaAtualizacao(LocalDateTime.now());
		});
		return encontrada;
	}

	public synchronized boolean excluir(Long codigo) { return ordens.removeIf(ordem -> ordem.getCodigo().equals(codigo)); }
}
