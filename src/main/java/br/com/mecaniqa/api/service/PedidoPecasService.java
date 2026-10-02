package br.com.mecaniqa.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.mecaniqa.api.dto.AtualizarStatusPedidoRequest;
import br.com.mecaniqa.api.dto.CriarPedidoPecasRequest;
import br.com.mecaniqa.api.dto.ItemPedidoRequest;
import br.com.mecaniqa.api.dto.PedidoPecasResponse;
import br.com.mecaniqa.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniqa.api.mapper.PedidoPecasMapper;
import br.com.mecaniqa.api.model.Peca;
import br.com.mecaniqa.api.model.PedidoPecas;
import br.com.mecaniqa.api.repository.PecaRepository;
import br.com.mecaniqa.api.repository.PedidoPecasRepository;

@Service
public class PedidoPecasService {

	private final PedidoPecasRepository pedidoRepository;
	private final PecaRepository pecaRepository;

	public PedidoPecasService() {
		this.pedidoRepository = PedidoPecasRepository.getInstance();
		this.pecaRepository = PecaRepository.getInstance();
	}

	public PedidoPecasResponse criar(CriarPedidoPecasRequest request) {
		PedidoPecas pedido = PedidoPecasMapper.toEntity(request);
		for (ItemPedidoRequest item : request.getItens()) {
			pedido.adicionarItem(buscarPeca(item.getCodigoPeca()), item.getQuantidade());
		}
		return PedidoPecasMapper.toResponse(pedidoRepository.salvar(pedido));
	}

	public List<PedidoPecasResponse> listar() {
		return pedidoRepository.listarTodos().stream()
				.map(PedidoPecasMapper::toResponse)
				.toList();
	}

	public PedidoPecasResponse buscar(Long codigo) {
		return PedidoPecasMapper.toResponse(buscarPedido(codigo));
	}

	public PedidoPecasResponse adicionarItem(Long codigo, ItemPedidoRequest request) {
		buscarPedido(codigo);
		Peca peca = buscarPeca(request.getCodigoPeca());
		PedidoPecas pedido = pedidoRepository.adicionarItem(codigo, peca, request.getQuantidade())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Pedido de peças não encontrado"));
		return PedidoPecasMapper.toResponse(pedido);
	}

	public PedidoPecasResponse atualizarStatus(Long codigo, AtualizarStatusPedidoRequest request) {
		PedidoPecas pedido = pedidoRepository.atualizarStatus(codigo, request.getStatus())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Pedido de peças não encontrado"));
		return PedidoPecasMapper.toResponse(pedido);
	}

	private PedidoPecas buscarPedido(Long codigo) {
		return pedidoRepository.buscarPorCodigo(codigo)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Pedido de peças não encontrado"));
	}

	private Peca buscarPeca(Long codigo) {
		return pecaRepository.buscarPorCodigo(codigo)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada: " + codigo));
	}
}
