package br.com.mecaniqa.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Peca {

	private Long codigo;
	private String codigoBarras;
	private String fornecedorMarca;
	private Integer quantidadeEstoque;
	private BigDecimal precoCusto;
	private BigDecimal precoVenda;
	private LocalDateTime dataCadastro;
	private LocalDateTime dataUltimaAtualizacao;
	private String tamanho;
	private String cor;
	private CategoriaPeca categoria;

	public Peca() {
	}

	public Peca(
			String codigoBarras,
			String fornecedorMarca,
			Integer quantidadeEstoque,
			BigDecimal precoCusto,
			BigDecimal precoVenda,
			String tamanho,
			String cor,
			CategoriaPeca categoria) {
		this.codigoBarras = codigoBarras;
		this.fornecedorMarca = fornecedorMarca;
		this.quantidadeEstoque = quantidadeEstoque;
		this.precoCusto = precoCusto;
		this.precoVenda = precoVenda;
		this.tamanho = tamanho;
		this.cor = cor;
		this.categoria = categoria;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

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

	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public LocalDateTime getDataUltimaAtualizacao() {
		return dataUltimaAtualizacao;
	}

	public void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao) {
		this.dataUltimaAtualizacao = dataUltimaAtualizacao;
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
