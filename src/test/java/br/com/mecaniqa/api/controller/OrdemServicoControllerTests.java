package br.com.mecaniqa.api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.mecaniqa.api.dto.CriarOrdemServicoRequest;
import br.com.mecaniqa.api.dto.OrdemServicoResponse;
import br.com.mecaniqa.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniqa.api.model.StatusOrdemServico;
import br.com.mecaniqa.api.service.OrdemDeServicoService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class OrdemServicoControllerTests {
  private OrdemDeServicoService service;
  private MockMvc mvc;

  @BeforeEach
  void setup() {
    service = mock(OrdemDeServicoService.class);
    mvc = MockMvcBuilders.standaloneSetup(new OrdemServicoController(service)).build();
  }

  @Test
  void excluirOrdemInexistenteRetorna404() throws Exception {
    doThrow(new RecursoNaoEncontradoException("Ordem de serviço não encontrada"))
        .when(service)
        .excluir(99999L);
    mvc.perform(delete("/api/ordens-servico/99999")).andExpect(status().isNotFound());
  }

  @Test
  void criarOrdemRetornaRelacionamentosEStatusInicial() throws Exception {
    LocalDateTime agora = LocalDateTime.now();
    when(service.criar(any(CriarOrdemServicoRequest.class)))
        .thenReturn(
            new OrdemServicoResponse(
                22L,
                "Ana",
                "ABC-1234",
                "Reparo",
                StatusOrdemServico.ABERTO,
                List.of(),
                List.of(),
                agora,
                agora));

    mvc.perform(
            post("/api/ordens-servico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"cliente":"Ana","veiculo":"ABC-1234","descricao":"Reparo",
                     "codigosPecas":[],"codigosServicos":[]}
                    """))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/api/ordens-servico/22"))
        .andExpect(jsonPath("$.status").value("ABERTO"))
        .andExpect(jsonPath("$.pecas").isArray())
        .andExpect(jsonPath("$.servicos").isArray());
  }

  @Test
  void dadosInvalidosRetornam400SemChamarService() throws Exception {
    mvc.perform(
            post("/api/ordens-servico")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"cliente\":\"\",\"veiculo\":\"\",\"descricao\":\"\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.campos.cliente").exists());
    verifyNoInteractions(service);
  }
}
