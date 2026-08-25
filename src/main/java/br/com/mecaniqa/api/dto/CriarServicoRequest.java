package br.com.mecaniqa.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CriarServicoRequest {

	@NotBlank(message = "é obrigatório")
	private String nome;

	@NotNull(message = "é obrigatória")
	@Positive(message = "deve ser maior que zero")
	private Integer duracaoEstimadaMinutos;

	@NotNull(message = "é obrigatório")
	@DecimalMin(value = "0.0", inclusive = true, message = "deve ser maior ou igual a zero")
	private BigDecimal custoTabelado;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

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
