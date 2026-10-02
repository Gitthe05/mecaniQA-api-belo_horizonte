package br.com.mecaniqa.api.dto;

import br.com.mecaniqa.api.model.StatusOrdemServico;
import jakarta.validation.constraints.NotNull;

public class AtualizarStatusOrdemServicoRequest {
	@NotNull(message = "é obrigatório")
	private StatusOrdemServico status;

	public AtualizarStatusOrdemServicoRequest() {
	}

	public StatusOrdemServico getStatus() { return status; }
	public void setStatus(StatusOrdemServico status) { this.status = status; }
}
