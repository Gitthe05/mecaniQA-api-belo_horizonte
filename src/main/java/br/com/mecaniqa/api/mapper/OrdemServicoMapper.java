package br.com.mecaniqa.api.mapper;

import java.util.List;

import br.com.mecaniqa.api.dto.CriarOrdemServicoRequest;
import br.com.mecaniqa.api.dto.OrdemServicoResponse;
import br.com.mecaniqa.api.model.OrdemDeServico;
import br.com.mecaniqa.api.model.Peca;
import br.com.mecaniqa.api.model.Servico;

public final class OrdemServicoMapper {
	private OrdemServicoMapper() { }
	public static OrdemDeServico toEntity(CriarOrdemServicoRequest dto, List<Peca> pecas, List<Servico> servicos) {
		return OrdemDeServico.builder().cliente(dto.getCliente()).veiculo(dto.getVeiculo())
				.descricao(dto.getDescricao()).pecas(pecas).servicos(servicos).build();
	}
	public static OrdemServicoResponse toResponse(OrdemDeServico ordem) {
		return new OrdemServicoResponse(ordem.getCodigo(), ordem.getCliente(), ordem.getVeiculo(),
				ordem.getDescricao(), ordem.getStatus(), ordem.getPecas().stream().map(PecaMapper::toResponse).toList(),
				ordem.getServicos().stream().map(ServicoMapper::toResponse).toList(), ordem.getDataCriacao(),
				ordem.getDataUltimaAtualizacao());
	}
}
