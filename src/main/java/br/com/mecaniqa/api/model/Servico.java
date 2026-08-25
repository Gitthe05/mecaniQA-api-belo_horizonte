package br.com.mecaniqa.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Servico {

	private Long codigo;
	private String nome;
	private Integer duracaoEstimadaMinutos;
	private BigDecimal custoTabelado;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataUltimaAtualizacao;

	public Servico() {
	}

	public Servico(String nome, Integer duracaoEstimadaMinutos, BigDecimal custoTabelado) {
		this.nome = nome;
		this.duracaoEstimadaMinutos = duracaoEstimadaMinutos;
		this.custoTabelado = custoTabelado;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

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
}
