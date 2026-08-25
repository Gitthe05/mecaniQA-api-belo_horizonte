package br.com.mecaniqa.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.mecaniqa.api.model.CategoriaPeca;
import br.com.mecaniqa.api.model.Peca;

class PecaRepositoryTests {

	private final PecaRepository repository = PecaRepository.getInstance();

	@Test
	void deveRetornarSempreAMesmaInstancia() {
		assertSame(repository, PecaRepository.getInstance());
		assertTrue(Modifier.isPrivate(PecaRepository.class.getDeclaredConstructors()[0].getModifiers()));
	}

	@Test
	void deveExecutarOCicloDeVidaDaPeca() {
		Peca peca = novaPeca();
		Peca salva = repository.salvar(peca);
		Long codigo = salva.getCodigo();

		try {
			assertNotNull(codigo);
			assertNotNull(salva.getDataCadastro());
			assertEquals(salva.getDataCadastro(), salva.getDataUltimaAtualizacao());
			assertTrue(repository.buscarPorCodigo(codigo).isPresent());

			LocalDateTime atualizacaoAnterior = salva.getDataUltimaAtualizacao();
			Peca atualizada = repository.atualizar(
					codigo,
					8,
					new BigDecimal("90.00"),
					new BigDecimal("140.00"))
					.orElseThrow();

			assertEquals(8, atualizada.getQuantidadeEstoque());
			assertEquals(new BigDecimal("90.00"), atualizada.getPrecoCusto());
			assertEquals(new BigDecimal("140.00"), atualizada.getPrecoVenda());
			assertFalse(atualizada.getDataUltimaAtualizacao().isBefore(atualizacaoAnterior));
			assertEquals("7890000000001", atualizada.getCodigoBarras());
			assertEquals("Fornecedor Teste", atualizada.getFornecedorMarca());
			assertEquals(CategoriaPeca.MOTOR, atualizada.getCategoria());
			assertEquals(salva.getDataCadastro(), atualizada.getDataCadastro());

			assertTrue(repository.excluir(codigo));
			assertTrue(repository.buscarPorCodigo(codigo).isEmpty());
			assertFalse(repository.excluir(codigo));
		} finally {
			repository.excluir(codigo);
		}
	}

	@Test
	void deveGerarCodigosUnicosERecusarAtualizacaoInexistente() {
		Peca primeira = repository.salvar(novaPeca());
		Peca segunda = repository.salvar(novaPeca());
		try {
			assertFalse(primeira.getCodigo().equals(segunda.getCodigo()));
			assertTrue(segunda.getCodigo() > primeira.getCodigo());
			assertTrue(repository.atualizar(
					999999L,
					1,
					BigDecimal.ZERO,
					BigDecimal.ZERO).isEmpty());
		} finally {
			repository.excluir(primeira.getCodigo());
			repository.excluir(segunda.getCodigo());
		}
	}

	@Test
	void deveProtegerAListaInternaContraInclusoesExternas() {
		Peca salva = repository.salvar(novaPeca());
		try {
			List<Peca> pecas = repository.listarTodos();
			assertThrows(UnsupportedOperationException.class, () -> pecas.add(novaPeca()));
		} finally {
			repository.excluir(salva.getCodigo());
		}
	}

	private Peca novaPeca() {
		return new Peca(
				"7890000000001",
				"Fornecedor Teste",
				5,
				new BigDecimal("80.00"),
				new BigDecimal("120.00"),
				null,
				null,
				CategoriaPeca.MOTOR);
	}
}
