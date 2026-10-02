package br.com.mecaniqa.api.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class CriarPedidoPecasRequest {
	@NotBlank(message = "é obrigatório")
	private String fornecedor;
	private List<@Valid ItemPedidoRequest> itens = new ArrayList<>();

	public CriarPedidoPecasRequest() {
	}
	public String getFornecedor() { return fornecedor; }
	public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }
	public List<ItemPedidoRequest> getItens() { return itens == null ? List.of() : itens; }
	public void setItens(List<ItemPedidoRequest> itens) { this.itens = itens; }
}
