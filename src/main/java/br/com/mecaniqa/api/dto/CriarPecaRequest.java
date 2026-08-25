package br.com.mecaniqa.api.dto;

import java.math.BigDecimal;

import br.com.mecaniqa.api.model.CategoriaPeca;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class CriarPecaRequest {

	@NotBlank(message = "é obrigatório")
	private String codigoBarras;

	@NotBlank(message = "é obrigatório")
	private String fornecedorMarca;

	@NotNull(message = "é obrigatória")
	@PositiveOrZero(message = "deve ser maior ou igual a zero")
	private Integer quantidadeEstoque;

	@NotNull(message = "é obrigatório")
	@DecimalMin(value = "0.0", inclusive = true, message = "deve ser maior ou igual a zero")
	private BigDecimal precoCusto;

	@NotNull(message = "é obrigatório")
	@DecimalMin(value = "0.0", inclusive = true, message = "deve ser maior ou igual a zero")
	private BigDecimal precoVenda;

	private String tamanho;
	private String cor;

	@NotNull(message = "é obrigatória")
	private CategoriaPeca categoria;

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public String getFornecedorMarca() {
		return fornecedorMarca;
	}

	public void setFornecedorMarca(String fornecedorMarca) {
		this.fornecedorMarca = fornecedorMarca;
	}

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

	public String getTamanho() {
		return tamanho;
	}

	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public CategoriaPeca getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaPeca categoria) {
		this.categoria = categoria;
	}
}
