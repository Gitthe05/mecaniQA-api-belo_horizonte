package br.com.mecaniqa.api.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class CriarOrdemServicoRequest {
	@NotBlank(message = "é obrigatório")
	private String cliente;
	@NotBlank(message = "é obrigatório")
	private String veiculo;
	@NotBlank(message = "é obrigatória")
	private String descricao;
	private List<Long> codigosPecas = new ArrayList<>();
	private List<Long> codigosServicos = new ArrayList<>();

	public CriarOrdemServicoRequest() {
	}

	public String getCliente() { return cliente; }
	public void setCliente(String cliente) { this.cliente = cliente; }
	public String getVeiculo() { return veiculo; }
	public void setVeiculo(String veiculo) { this.veiculo = veiculo; }
	public String getDescricao() { return descricao; }
	public void setDescricao(String descricao) { this.descricao = descricao; }
	public List<Long> getCodigosPecas() { return codigosPecas == null ? List.of() : codigosPecas; }
	public void setCodigosPecas(List<Long> codigosPecas) { this.codigosPecas = codigosPecas; }
	public List<Long> getCodigosServicos() { return codigosServicos == null ? List.of() : codigosServicos; }
	public void setCodigosServicos(List<Long> codigosServicos) { this.codigosServicos = codigosServicos; }
}
