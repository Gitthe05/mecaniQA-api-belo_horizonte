package br.com.mecaniqa.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.mecaniqa.api.model.Servico;

class ServicoRepositoryTests {

	private final ServicoRepository repository = ServicoRepository.getInstance();

	@Test
	void deveRetornarSempreAMesmaInstancia() {
		assertSame(repository, ServicoRepository.getInstance());
		assertTrue(Modifier.isPrivate(ServicoRepository.class.getDeclaredConstructors()[0].getModifiers()));
	}

	@Test
	void deveExecutarOCicloDeVidaDoServico() {
		Servico servico = new Servico("Alinhamento", 45, new BigDecimal("100.00"));
		Servico salvo = repository.salvar(servico);
		Long codigo = salvo.getCodigo();

		try {
			assertNotNull(codigo);
			assertNotNull(salvo.getDataCriacao());
			assertEquals(salvo.getDataCriacao(), salvo.getDataUltimaAtualizacao());
			assertTrue(repository.buscarPorCodigo(codigo).isPresent());

			LocalDateTime atualizacaoAnterior = salvo.getDataUltimaAtualizacao();
			Servico atualizado = repository.atualizar(
					codigo,
					60,
					new BigDecimal("130.00"))
					.orElseThrow();

			assertEquals(60, atualizado.getDuracaoEstimadaMinutos());
			assertEquals(new BigDecimal("130.00"), atualizado.getCustoTabelado());
			assertFalse(atualizado.getDataUltimaAtualizacao().isBefore(atualizacaoAnterior));
			assertEquals("Alinhamento", atualizado.getNome());
			assertEquals(salvo.getDataCriacao(), atualizado.getDataCriacao());

			assertTrue(repository.excluir(codigo));
			assertTrue(repository.buscarPorCodigo(codigo).isEmpty());
			assertFalse(repository.excluir(codigo));
		} finally {
			repository.excluir(codigo);
		}
	}

	@Test
	void deveGerarCodigosUnicosERecusarAtualizacaoInexistente() {
		Servico primeiro = repository.salvar(new Servico("Alinhamento", 45, new BigDecimal("100.00")));
		Servico segundo = repository.salvar(new Servico("Balanceamento", 30, new BigDecimal("80.00")));
		try {
			assertFalse(primeiro.getCodigo().equals(segundo.getCodigo()));
			assertTrue(segundo.getCodigo() > primeiro.getCodigo());
			assertTrue(repository.atualizar(999999L, 1, BigDecimal.ZERO).isEmpty());
		} finally {
			repository.excluir(primeiro.getCodigo());
			repository.excluir(segundo.getCodigo());
		}
	}

	@Test
	void deveProtegerAListaInternaContraInclusoesExternas() {
		Servico salvo = repository.salvar(new Servico("Alinhamento", 45, new BigDecimal("100.00")));
		try {
			List<Servico> servicos = repository.listarTodos();
			assertThrows(
					UnsupportedOperationException.class,
					() -> servicos.add(new Servico("Novo", 10, BigDecimal.ZERO)));
		} finally {
			repository.excluir(salvo.getCodigo());
		}
	}
}
