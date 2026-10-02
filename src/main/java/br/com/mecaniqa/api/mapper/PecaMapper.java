package br.com.mecaniqa.api.mapper;

import br.com.mecaniqa.api.dto.CriarPecaRequest;
import br.com.mecaniqa.api.dto.PecaResponse;
import br.com.mecaniqa.api.model.Peca;

public final class PecaMapper {
	private PecaMapper() { }
	public static Peca toEntity(CriarPecaRequest dto) {
		return new Peca(dto.getCodigoBarras(), dto.getFornecedorMarca(), dto.getQuantidadeEstoque(),
				dto.getPrecoCusto(), dto.getPrecoVenda(), dto.getTamanho(), dto.getCor(), dto.getCategoria());
	}
	public static PecaResponse toResponse(Peca peca) {
		return new PecaResponse(peca.getCodigo(), peca.getCodigoBarras(), peca.getFornecedorMarca(),
				peca.getQuantidadeEstoque(), peca.getPrecoCusto(), peca.getPrecoVenda(), peca.getDataCadastro(),
				peca.getDataUltimaAtualizacao(), peca.getTamanho(), peca.getCor(), peca.getCategoria());
	}
}
