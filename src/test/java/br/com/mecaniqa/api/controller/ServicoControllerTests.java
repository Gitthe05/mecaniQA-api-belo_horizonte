package br.com.mecaniqa.api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.mecaniqa.api.dto.CriarServicoRequest;
import br.com.mecaniqa.api.dto.ServicoResponse;
import br.com.mecaniqa.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniqa.api.service.ServicoService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ServicoControllerTests {
  private ServicoService service;
  private MockMvc mvc;

  @BeforeEach
  void setup() {
    service = mock(ServicoService.class);
    mvc = MockMvcBuilders.standaloneSetup(new ServicoController(service)).build();
  }

  @Test
  void servicoInexistenteRetorna404() throws Exception {
    when(service.buscar(99999L))
        .thenThrow(new RecursoNaoEncontradoException("Serviço não encontrado"));
    mvc.perform(get("/api/servicos/99999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Serviço não encontrado"));
  }

  @Test
  void cadastroValidoRetorna201ComLocalizacao() throws Exception {
    LocalDateTime agora = LocalDateTime.now();
    when(service.criar(any(CriarServicoRequest.class)))
        .thenReturn(new ServicoResponse(11L, "Alinhamento", 45, BigDecimal.TEN, agora, agora));

    mvc.perform(
            post("/api/servicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"nome":"Alinhamento","duracaoEstimadaMinutos":45,"custoTabelado":10}
                    """))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/api/servicos/11"))
        .andExpect(jsonPath("$.nome").value("Alinhamento"));
  }

  @Test
  void dadosInvalidosRetornam400SemChamarService() throws Exception {
    mvc.perform(
            post("/api/servicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"\",\"duracaoEstimadaMinutos\":0,\"custoTabelado\":-1}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.campos.nome").exists())
        .andExpect(jsonPath("$.campos.duracaoEstimadaMinutos").exists())
        .andExpect(jsonPath("$.campos.custoTabelado").exists());
    verifyNoInteractions(service);
  }
}
