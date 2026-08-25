package br.com.mecaniqa.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class AtualizarPecaRequest {

	@NotNull(message = "é obrigatória")
	@PositiveOrZero(message = "deve ser maior ou igual a zero")
	private Integer quantidadeEstoque;

	@NotNull(message = "é obrigatório")
	@DecimalMin(value = "0.0", inclusive = true, message = "deve ser maior ou igual a zero")
	private BigDecimal precoCusto;

	@NotNull(message = "é obrigatório")
	@DecimalMin(value = "0.0", inclusive = true, message = "deve ser maior ou igual a zero")
	private BigDecimal precoVenda;

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public BigDecimal getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(BigDecimal precoCusto) {
		this.precoCusto = precoCusto;
	}

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}
}
