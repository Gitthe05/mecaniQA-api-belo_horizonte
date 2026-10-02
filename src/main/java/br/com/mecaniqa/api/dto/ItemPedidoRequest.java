package br.com.mecaniqa.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ItemPedidoRequest {
	@NotNull(message = "é obrigatório")
	private Long codigoPeca;
	@NotNull(message = "é obrigatória")
	@Positive(message = "deve ser maior que zero")
	private Integer quantidade;

	public ItemPedidoRequest() {
	}
	public Long getCodigoPeca() { return codigoPeca; }
	public void setCodigoPeca(Long codigoPeca) { this.codigoPeca = codigoPeca; }
	public Integer getQuantidade() { return quantidade; }
	public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
