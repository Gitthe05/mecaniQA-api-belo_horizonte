package br.com.mecaniqa.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.com.mecaniqa.api.model.StatusOrdemServico;

public class OrdemServicoResponse {
	private final Long codigo;
	private final String cliente;
	private final String veiculo;
	private final String descricao;
	private final StatusOrdemServico status;
	private final List<PecaResponse> pecas;
	private final List<ServicoResponse> servicos;
	private final LocalDateTime dataCriacao;
	private final LocalDateTime dataUltimaAtualizacao;

	public OrdemServicoResponse(Long codigo, String cliente, String veiculo, String descricao,
			StatusOrdemServico status, List<PecaResponse> pecas, List<ServicoResponse> servicos,
			LocalDateTime dataCriacao, LocalDateTime dataUltimaAtualizacao) {
		this.codigo = codigo; this.cliente = cliente; this.veiculo = veiculo; this.descricao = descricao;
		this.status = status; this.pecas = pecas; this.servicos = servicos;
		this.dataCriacao = dataCriacao; this.dataUltimaAtualizacao = dataUltimaAtualizacao;
	}
	public Long getCodigo() { return codigo; }
	public String getCliente() { return cliente; }
	public String getVeiculo() { return veiculo; }
	public String getDescricao() { return descricao; }
	public StatusOrdemServico getStatus() { return status; }
	public List<PecaResponse> getPecas() { return pecas; }
	public List<ServicoResponse> getServicos() { return servicos; }
	public LocalDateTime getDataCriacao() { return dataCriacao; }
	public LocalDateTime getDataUltimaAtualizacao() { return dataUltimaAtualizacao; }
}
