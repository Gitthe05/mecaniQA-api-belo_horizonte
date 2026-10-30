package br.com.mecaniqa.api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.mecaniqa.api.dto.CriarPecaRequest;
import br.com.mecaniqa.api.dto.PecaResponse;
import br.com.mecaniqa.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniqa.api.model.CategoriaPeca;
import br.com.mecaniqa.api.service.PecaService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class PecaControllerTests {
  private PecaService service;
  private MockMvc mvc;

  @BeforeEach
  void setup() {
    service = mock(PecaService.class);
    mvc = MockMvcBuilders.standaloneSetup(new PecaController(service)).build();
  }

  @Test
  void recursoInexistenteRetorna404() throws Exception {
    when(service.buscar(99999L))
        .thenThrow(new RecursoNaoEncontradoException("Peça não encontrada"));
    mvc.perform(get("/api/pecas/99999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Peça não encontrada"));
  }

  @Test
  void dadosInvalidosSaoTratadosManualmentePeloController() throws Exception {
    mvc.perform(post("/api/pecas").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Corpo da requisição inválido"))
        .andExpect(jsonPath("$.campos.codigoBarras").exists());
    verifyNoInteractions(service);
  }

  @Test
  void cadastroValidoRetorna201ComLocalizacao() throws Exception {
    LocalDateTime agora = LocalDateTime.now();
    when(service.criar(any(CriarPecaRequest.class)))
        .thenReturn(
            new PecaResponse(
                17L,
                "789123",
                "Bosch",
                3,
                new BigDecimal("20.00"),
                new BigDecimal("35.00"),
                agora,
                agora,
                null,
                null,
                CategoriaPeca.FREIOS));

    mvc.perform(
            post("/api/pecas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"codigoBarras":"789123","fornecedorMarca":"Bosch","quantidadeEstoque":3,
                     "precoCusto":20,"precoVenda":35,"categoria":"FREIOS"}
                    """))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/api/pecas/17"))
        .andExpect(jsonPath("$.codigoBarras").value("789123"));
  }
}
