package br.com.mecaniqa.api.model;

import java.util.Objects;

public class ItemPedidoPeca {

	private PedidoPecas pedido;
	private Peca peca;
	private Integer quantidade;

	public ItemPedidoPeca() {
	}

	public ItemPedidoPeca(PedidoPecas pedido, Peca peca, Integer quantidade) {
		this.pedido = pedido;
		this.peca = peca;
		this.quantidade = quantidade;
	}

	public PedidoPecas getPedido() {
		return pedido;
	}

	public void setPedido(PedidoPecas pedido) {
		this.pedido = pedido;
	}

	public Peca getPeca() {
		return peca;
	}

	public void setPeca(Peca peca) {
		this.peca = peca;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	@Override
	public boolean equals(Object objeto) {
		if (this == objeto) {
			return true;
		}
		if (!(objeto instanceof ItemPedidoPeca outro)) {
			return false;
		}
		return Objects.equals(pedido, outro.pedido) && Objects.equals(peca, outro.peca);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pedido, peca);
	}
}
