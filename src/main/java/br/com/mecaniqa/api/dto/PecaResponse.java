package br.com.mecaniqa.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.mecaniqa.api.model.CategoriaPeca;

public class PecaResponse {
	private final Long codigo;
	private final String codigoBarras;
	private final String fornecedorMarca;
	private final Integer quantidadeEstoque;
	private final BigDecimal precoCusto;
	private final BigDecimal precoVenda;
	private final LocalDateTime dataCadastro;
	private final LocalDateTime dataUltimaAtualizacao;
	private final String tamanho;
	private final String cor;
	private final CategoriaPeca categoria;

	public PecaResponse(Long codigo, String codigoBarras, String fornecedorMarca, Integer quantidadeEstoque,
			BigDecimal precoCusto, BigDecimal precoVenda, LocalDateTime dataCadastro,
			LocalDateTime dataUltimaAtualizacao, String tamanho, String cor, CategoriaPeca categoria) {
		this.codigo = codigo; this.codigoBarras = codigoBarras; this.fornecedorMarca = fornecedorMarca;
		this.quantidadeEstoque = quantidadeEstoque; this.precoCusto = precoCusto; this.precoVenda = precoVenda;
		this.dataCadastro = dataCadastro; this.dataUltimaAtualizacao = dataUltimaAtualizacao;
		this.tamanho = tamanho; this.cor = cor; this.categoria = categoria;
	}
	public Long getCodigo() { return codigo; }
	public String getCodigoBarras() { return codigoBarras; }
	public String getFornecedorMarca() { return fornecedorMarca; }
	public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
	public BigDecimal getPrecoCusto() { return precoCusto; }
	public BigDecimal getPrecoVenda() { return precoVenda; }
	public LocalDateTime getDataCadastro() { return dataCadastro; }
	public LocalDateTime getDataUltimaAtualizacao() { return dataUltimaAtualizacao; }
	public String getTamanho() { return tamanho; }
	public String getCor() { return cor; }
	public CategoriaPeca getCategoria() { return categoria; }
}
