package br.com.mecaniqa.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PedidoPecas {

	private Long codigo;
	private String fornecedor;
	private StatusPedidoPecas status;
	private final List<ItemPedidoPeca> itens = new ArrayList<>();
	private LocalDateTime dataCriacao;
	private LocalDateTime dataUltimaAtualizacao;

	public PedidoPecas() {
	}

	public PedidoPecas(String fornecedor) {
		this.fornecedor = fornecedor;
		this.status = StatusPedidoPecas.ORCANDO;
	}

	public void adicionarItem(Peca peca, Integer quantidade) {
		if (peca == null || quantidade == null || quantidade <= 0) {
			throw new IllegalArgumentException("Peça e quantidade positiva são obrigatórias");
		}
		for (ItemPedidoPeca item : itens) {
			if (item.getPeca().getCodigo().equals(peca.getCodigo())) {
				item.setQuantidade(item.getQuantidade() + quantidade);
				return;
			}
		}
		itens.add(new ItemPedidoPeca(this, peca, quantidade));
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public StatusPedidoPecas getStatus() {
		return status;
	}

	public void setStatus(StatusPedidoPecas status) {
		this.status = status;
	}

	public List<ItemPedidoPeca> getItens() {
		return List.copyOf(itens);
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDateTime getDataUltimaAtualizacao() {
		return dataUltimaAtualizacao;
	}

	public void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao) {
		this.dataUltimaAtualizacao = dataUltimaAtualizacao;
	}

	@Override
	public boolean equals(Object objeto) {
		if (this == objeto) {
			return true;
		}
		if (!(objeto instanceof PedidoPecas outro)) {
			return false;
		}
		return codigo != null && Objects.equals(codigo, outro.codigo);
	}

	@Override
	public int hashCode() {
		return codigo == null ? 0 : Objects.hash(codigo);
	}
}
