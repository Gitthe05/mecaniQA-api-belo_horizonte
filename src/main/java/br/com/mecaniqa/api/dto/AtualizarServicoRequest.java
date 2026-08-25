package br.com.mecaniqa.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AtualizarServicoRequest {

	@NotNull(message = "é obrigatória")
	@Positive(message = "deve ser maior que zero")
	private Integer duracaoEstimadaMinutos;

	@NotNull(message = "é obrigatório")
	@DecimalMin(value = "0.0", inclusive = true, message = "deve ser maior ou igual a zero")
	private BigDecimal custoTabelado;

	public Integer getDuracaoEstimadaMinutos() {
		return duracaoEstimadaMinutos;
	}

	public void setDuracaoEstimadaMinutos(Integer duracaoEstimadaMinutos) {
		this.duracaoEstimadaMinutos = duracaoEstimadaMinutos;
	}

	public BigDecimal getCustoTabelado() {
		return custoTabelado;
	}

	public void setCustoTabelado(BigDecimal custoTabelado) {
		this.custoTabelado = custoTabelado;
	}
}
