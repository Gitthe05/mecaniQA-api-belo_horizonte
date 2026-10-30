package br.com.mecaniqa.api.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ServicoRepositoryTests {
  @Test
  void repositorioImplementaContratoGenericoJDBC() {
    assertTrue(Repositorio.class.isAssignableFrom(ServicoRepository.class));
    assertTrue(RepositorioJdbcTemplate.class.isAssignableFrom(ServicoRepository.class));
  }

  @Test
  void outrosRepositoriosJDBCImplementamContrato() {
    assertTrue(Repositorio.class.isAssignableFrom(OrdemServicoRepository.class));
    assertTrue(Repositorio.class.isAssignableFrom(PedidoPecasRepository.class));
    assertTrue(Repositorio.class.isAssignableFrom(PecaJdbcRepository.class));
  }
}
