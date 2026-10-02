package br.com.mecaniqa.api.dto;

public class ItemPedidoResponse {
	private final PecaResponse peca;
	private final Integer quantidade;
	public ItemPedidoResponse(PecaResponse peca, Integer quantidade) {
		this.peca = peca; this.quantidade = quantidade;
	}
	public PecaResponse getPeca() { return peca; }
	public Integer getQuantidade() { return quantidade; }
}
