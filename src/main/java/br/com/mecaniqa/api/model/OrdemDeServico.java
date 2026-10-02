package br.com.mecaniqa.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrdemDeServico {

	private Long codigo;
	private String cliente;
	private String veiculo;
	private String descricao;
	private StatusOrdemServico status;
	private List<Peca> pecas;
	private List<Servico> servicos;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataUltimaAtualizacao;

	private OrdemDeServico(Builder builder) {
		this.cliente = builder.cliente;
		this.veiculo = builder.veiculo;
		this.descricao = builder.descricao;
		this.status = builder.status;
		this.pecas = new ArrayList<>(builder.pecas);
		this.servicos = new ArrayList<>(builder.servicos);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String cliente;
		private String veiculo;
		private String descricao;
		private StatusOrdemServico status = StatusOrdemServico.ABERTO;
		private List<Peca> pecas = new ArrayList<>();
		private List<Servico> servicos = new ArrayList<>();

		public Builder cliente(String cliente) {
			this.cliente = cliente;
			return this;
		}

		public Builder veiculo(String veiculo) {
			this.veiculo = veiculo;
			return this;
		}

		public Builder descricao(String descricao) {
			this.descricao = descricao;
			return this;
		}

		public Builder status(StatusOrdemServico status) {
			this.status = status;
			return this;
		}

		public Builder pecas(List<Peca> pecas) {
			this.pecas = pecas == null ? new ArrayList<>() : new ArrayList<>(pecas);
			return this;
		}

		public Builder servicos(List<Servico> servicos) {
			this.servicos = servicos == null ? new ArrayList<>() : new ArrayList<>(servicos);
			return this;
		}

		public OrdemDeServico build() {
			if (cliente == null || cliente.isBlank() || veiculo == null || veiculo.isBlank()
					|| descricao == null || descricao.isBlank() || status == null) {
				throw new IllegalStateException("Cliente, veículo, descrição e status são obrigatórios");
			}
			return new OrdemDeServico(this);
		}
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(String veiculo) {
		this.veiculo = veiculo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public StatusOrdemServico getStatus() {
		return status;
	}

	public void setStatus(StatusOrdemServico status) {
		this.status = status;
	}

	public List<Peca> getPecas() {
		return List.copyOf(pecas);
	}

	public void setPecas(List<Peca> pecas) {
		this.pecas = new ArrayList<>(pecas);
	}

	public List<Servico> getServicos() {
		return List.copyOf(servicos);
	}

	public void setServicos(List<Servico> servicos) {
		this.servicos = new ArrayList<>(servicos);
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

	@Override
	public boolean equals(Object objeto) {
		if (this == objeto) {
			return true;
		}
		if (!(objeto instanceof OrdemDeServico outra)) {
			return false;
		}
		return codigo != null && Objects.equals(codigo, outra.codigo);
	}

	@Override
	public int hashCode() {
		return codigo == null ? 0 : Objects.hash(codigo);
	}
}
