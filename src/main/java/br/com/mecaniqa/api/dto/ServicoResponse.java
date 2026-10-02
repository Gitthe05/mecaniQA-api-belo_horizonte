package br.com.mecaniqa.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ServicoResponse {
	private final Long codigo;
	private final String nome;
	private final Integer duracaoEstimadaMinutos;
	private final BigDecimal custoTabelado;
	private final LocalDateTime dataCriacao;
	private final LocalDateTime dataUltimaAtualizacao;

	public ServicoResponse(Long codigo, String nome, Integer duracaoEstimadaMinutos, BigDecimal custoTabelado,
			LocalDateTime dataCriacao, LocalDateTime dataUltimaAtualizacao) {
		this.codigo = codigo; this.nome = nome; this.duracaoEstimadaMinutos = duracaoEstimadaMinutos;
		this.custoTabelado = custoTabelado; this.dataCriacao = dataCriacao;
		this.dataUltimaAtualizacao = dataUltimaAtualizacao;
	}
	public Long getCodigo() { return codigo; }
	public String getNome() { return nome; }
	public Integer getDuracaoEstimadaMinutos() { return duracaoEstimadaMinutos; }
	public BigDecimal getCustoTabelado() { return custoTabelado; }
	public LocalDateTime getDataCriacao() { return dataCriacao; }
	public LocalDateTime getDataUltimaAtualizacao() { return dataUltimaAtualizacao; }
}
