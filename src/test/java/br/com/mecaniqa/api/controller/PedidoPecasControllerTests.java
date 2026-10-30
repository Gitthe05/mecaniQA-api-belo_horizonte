package br.com.mecaniqa.api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.mecaniqa.api.dto.CriarPedidoPecasRequest;
import br.com.mecaniqa.api.dto.PedidoPecasResponse;
import br.com.mecaniqa.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniqa.api.model.StatusPedidoPecas;
import br.com.mecaniqa.api.service.PedidoPecasService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class PedidoPecasControllerTests {
  private PedidoPecasService service;
  private MockMvc mvc;

  @BeforeEach
  void setup() {
    service = mock(PedidoPecasService.class);
    mvc = MockMvcBuilders.standaloneSetup(new PedidoPecasController(service)).build();
  }

  @Test
  void excluirPedidoInexistenteRetorna404() throws Exception {
    doThrow(new RecursoNaoEncontradoException("Pedido de peças não encontrado"))
        .when(service)
        .excluir(99999L);
    mvc.perform(delete("/api/pedidos-pecas/99999")).andExpect(status().isNotFound());
  }

  @Test
  void cadastroValidoRetorna201ComPedidoOrcando() throws Exception {
    LocalDateTime agora = LocalDateTime.now();
    when(service.criar(any(CriarPedidoPecasRequest.class)))
        .thenReturn(
            new PedidoPecasResponse(
                31L, "Distribuidora X", StatusPedidoPecas.ORCANDO, List.of(), agora, agora));

    mvc.perform(
            post("/api/pedidos-pecas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fornecedor\":\"Distribuidora X\",\"itens\":[]}"))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/api/pedidos-pecas/31"))
        .andExpect(jsonPath("$.status").value("ORCANDO"));
  }

  @Test
  void quantidadeInvalidaRetorna400SemChamarService() throws Exception {
    mvc.perform(
            post("/api/pedidos-pecas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fornecedor\":\"X\",\"itens\":[{\"codigoPeca\":1,\"quantidade\":0}]}"))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(service);
  }
}
