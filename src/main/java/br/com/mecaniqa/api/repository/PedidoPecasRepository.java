package br.com.mecaniqa.api.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import br.com.mecaniqa.api.model.Peca;
import br.com.mecaniqa.api.model.PedidoPecas;
import br.com.mecaniqa.api.model.StatusPedidoPecas;

public final class PedidoPecasRepository {

	private static final PedidoPecasRepository INSTANCE = new PedidoPecasRepository();
	private final List<PedidoPecas> pedidos = new ArrayList<>();
	private final AtomicLong proximoCodigo = new AtomicLong(1);

	private PedidoPecasRepository() {
	}

	public static PedidoPecasRepository getInstance() { return INSTANCE; }

	public synchronized PedidoPecas salvar(PedidoPecas pedido) {
		LocalDateTime agora = LocalDateTime.now();
		pedido.setCodigo(proximoCodigo.getAndIncrement());
		pedido.setDataCriacao(agora);
		pedido.setDataUltimaAtualizacao(agora);
		pedidos.add(pedido);
		return pedido;
	}

	public synchronized List<PedidoPecas> listarTodos() { return List.copyOf(pedidos); }

	public synchronized Optional<PedidoPecas> buscarPorCodigo(Long codigo) {
		return pedidos.stream().filter(pedido -> pedido.getCodigo().equals(codigo)).findFirst();
	}

	public synchronized Optional<PedidoPecas> adicionarItem(Long codigo, Peca peca, Integer quantidade) {
		Optional<PedidoPecas> encontrado = buscarPorCodigo(codigo);
		encontrado.ifPresent(pedido -> {
			pedido.adicionarItem(peca, quantidade);
			pedido.setDataUltimaAtualizacao(LocalDateTime.now());
		});
		return encontrado;
	}

	public synchronized Optional<PedidoPecas> atualizarStatus(Long codigo, StatusPedidoPecas status) {
		Optional<PedidoPecas> encontrado = buscarPorCodigo(codigo);
		encontrado.ifPresent(pedido -> {
			pedido.setStatus(status);
			pedido.setDataUltimaAtualizacao(LocalDateTime.now());
		});
		return encontrado;
	}

	public synchronized boolean excluir(Long codigo) { return pedidos.removeIf(pedido -> pedido.getCodigo().equals(codigo)); }
}
