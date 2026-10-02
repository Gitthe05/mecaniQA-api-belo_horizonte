package br.com.mecaniqa.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.com.mecaniqa.api.model.StatusPedidoPecas;

public class PedidoPecasResponse {
	private final Long codigo;
	private final String fornecedor;
	private final StatusPedidoPecas status;
	private final List<ItemPedidoResponse> itens;
	private final LocalDateTime dataCriacao;
	private final LocalDateTime dataUltimaAtualizacao;
	public PedidoPecasResponse(Long codigo, String fornecedor, StatusPedidoPecas status,
			List<ItemPedidoResponse> itens, LocalDateTime dataCriacao, LocalDateTime dataUltimaAtualizacao) {
		this.codigo = codigo; this.fornecedor = fornecedor; this.status = status; this.itens = itens;
		this.dataCriacao = dataCriacao; this.dataUltimaAtualizacao = dataUltimaAtualizacao;
	}
	public Long getCodigo() { return codigo; }
	public String getFornecedor() { return fornecedor; }
	public StatusPedidoPecas getStatus() { return status; }
	public List<ItemPedidoResponse> getItens() { return itens; }
	public LocalDateTime getDataCriacao() { return dataCriacao; }
	public LocalDateTime getDataUltimaAtualizacao() { return dataUltimaAtualizacao; }
}
